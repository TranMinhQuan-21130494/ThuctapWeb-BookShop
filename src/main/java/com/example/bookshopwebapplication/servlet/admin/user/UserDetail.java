package com.example.bookshopwebapplication.servlet.admin.user;

import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/admin/userManager/detail")
public class UserDetail extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<String> id = Optional.ofNullable(request.getParameter("id"));
        if (id.isPresent()) {
            Optional<UserDto> user = Protector.of(() ->
                    userService.getById(Long.parseLong(id.get()))).get(Optional::empty);
            if (user.isPresent()) {
                request.setAttribute("user", user.get());
                request.getRequestDispatcher("/WEB-INF/views/admin/user/detail.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/userManager");
            }
        }else {
            response.sendRedirect(request.getContextPath() + "/admin/userManager");
        }
    }
}
