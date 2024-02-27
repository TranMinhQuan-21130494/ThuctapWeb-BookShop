package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.Protector;
import com.example.bookshopwebapplication.utils.mail.EmailUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/activeAccount")
public class ActiveAccount extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<String> email = Optional.ofNullable(request.getParameter("email"));
        Optional<String> code = Optional.ofNullable(request.getParameter("code"));

        if (EmailUtils.isValidVerificationCode(email.get(), code.get())) {
            UserDto user = (UserDto) request.getSession().getAttribute("userSignUp");
            Optional<UserDto> userSignUp = Protector.of(() -> userService.insert(user)).get(Optional::empty);

            if (userSignUp.isPresent()) {
                request.getSession().removeAttribute("userSignUp");
                response.sendRedirect(request.getContextPath() + "/signin");
            }
        }else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
