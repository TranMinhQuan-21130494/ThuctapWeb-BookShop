package com.example.bookshopwebapplication.filter;

import com.example.bookshopwebapplication.dto.UserDto;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@WebFilter(filterName = "AuthorizationFilter", value = "/admin/*")
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);

        String loginURI = request.getContextPath() + "/admin/signin";
        String admin401 = request.getContextPath() + "/admin/401";

        Optional<String> role = Optional.ofNullable(session).map(user -> (UserDto) session.getAttribute("currentUser")).map(UserDto::getRole);

        boolean isAdmin = role.map("ADMIN"::equals).orElse(false);
        boolean isEmployee = role.map("EMPLOYEE"::equals).orElse(false);
        boolean loginRequest = request.getRequestURI().equals(loginURI);

        Stream<String> restrictedPathsForEmployee = Stream.of("/admin/userManager")
                .map(path -> request.getContextPath() + path);
        boolean isNotAccessibleForEmployee = restrictedPathsForEmployee
                .anyMatch(s -> request.getRequestURI().startsWith(s));

        if (isAdmin || isEmployee || loginRequest) {
            if (isEmployee && isNotAccessibleForEmployee) {
                response.sendRedirect(admin401);
            } else {
                filterChain.doFilter(request, response);
            }
        }else {
            response.sendRedirect(loginURI);
        }
    }
}
