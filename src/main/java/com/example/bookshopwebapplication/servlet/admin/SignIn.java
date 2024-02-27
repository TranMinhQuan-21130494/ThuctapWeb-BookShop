package com.example.bookshopwebapplication.servlet.admin;

import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.EncodePassword;
import com.example.bookshopwebapplication.utils.Protector;
import com.example.bookshopwebapplication.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/admin/signin")
public class SignIn extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/admin/signin.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> values = new HashMap<>();
        values.put("username", request.getParameter("username"));
        values.put("password", request.getParameter("password"));

        Map<String, List<String>> violations = new HashMap<>();
        Optional<UserDto> userFromServer = Protector.of(() -> userService.getByUsername(values.get("username")))
                .get(Optional::empty);
        violations.put("usernameViolations", Validator.of(values.get("username"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(25)
                .isExistent(userFromServer.isPresent(), "Tên đăng nhập")
                .toList());
        violations.put("passwordViolations", Validator.of(values.get("password"))
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(32)
                .changeTo(EncodePassword.hash(values.get("password")))
                .isEqualTo(userFromServer.map(UserDto::getPassword).orElse(""), "Mật khẩu")
                .toList());

        int sumOfViolations = violations.values().stream().mapToInt(List::size).sum();

        if (sumOfViolations == 0 && userFromServer.isPresent()) {
            UserDto user = userFromServer.get();
            if (Arrays.asList("ADMIN", "EMPLOYEE").contains(user.getRole())) {
                request.getSession().setAttribute("currentUser", user);
                response.sendRedirect(request.getContextPath() + "/admin");
            } else {
                String errorMessage = "Người dùng không có quyền đăng nhập!";
                request.setAttribute("errorMessage", errorMessage);
                request.getRequestDispatcher("/WEB-INF/views/admin/signin.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("values", values);
            request.setAttribute("violations", violations);
            request.getRequestDispatcher("/WEB-INF/views/admin/signin.jsp").forward(request, response);
        }
    }
}
