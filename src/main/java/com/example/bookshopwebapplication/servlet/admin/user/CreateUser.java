package com.example.bookshopwebapplication.servlet.admin.user;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/admin/userManager/create")
public class CreateUser extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin/user/create.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user = new UserDto();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPhoneNumber(request.getParameter("phoneNumber"));
        user.setGender(Protector.of(() -> Integer.parseInt(request.getParameter("gender"))).get(0));
        user.setAddress(request.getParameter("address"));
        user.setRole(request.getParameter("role"));

        Map<String, List<String>> violations = new HashMap<>();
        Optional<UserDto> userByUsername = Protector.of(() -> userService.getByUsername(user.getUsername())).get(Optional::empty);
        Optional<UserDto> userByEmail = Protector.of(() -> userService.getByEmail(user.getEmail())).get(Optional::empty);
        Optional<UserDto> userByPhoneNumber = Protector.of(() -> userService.getByPhoneNumber(user.getPhoneNumber())).get(Optional::empty);
        violations.put("usernameViolations", Validator.of(user.getUsername())
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(25)
                .isNotExistent(userByUsername.isPresent(), "Tên đăng nhập")
                .toList());
        violations.put("passwordViolations", Validator.of(user.getPassword())
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(32)
                .toList());
        violations.put("fullNameViolations", Validator.of(user.getFullName())
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .toList());
        violations.put("emailViolations", Validator.of(user.getEmail())
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .hasPattern("^[^@]+@[^@]+\\.[^@]+$", "email")
                .isNotExistent(userByEmail.isPresent(), "Email")
                .toList());
        violations.put("phoneNumberViolations", Validator.of(user.getPhoneNumber())
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .hasPattern("^\\d{10,11}$", "số điện thoại")
                .isNotExistent(userByPhoneNumber.isPresent(), "Số điện thoại")
                .toList());
        violations.put("genderViolations", Validator.of(user.getGender())
                .isNotNull()
                .toList());
        violations.put("addressViolations", Validator.of(user.getAddress())
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .toList());
        violations.put("roleViolations", Validator.of(user.getRole())
                .isNotNull()
                .toList());

        int sumOfViolations = violations.values().stream().mapToInt(List::size).sum();
        String successMessage = "Thêm thành công!";
        String errorMessage = "Thêm thất bại!";

        if (sumOfViolations == 0) {
            user.setPassword(EncodePassword.hash(user.getPassword()));
            Protector.of(() -> userService.insert(user))
                    .done(r -> request.getSession().setAttribute("successMessage", successMessage))
                    .fail(e -> {
                        request.setAttribute("user", user);
                        request.getSession().setAttribute("errorMessage", errorMessage);
                    });
        } else {
            request.setAttribute("user", user);
            request.setAttribute("violations", violations);
        }
        request.getRequestDispatcher("/WEB-INF/views/admin/user/create.jsp").forward(request, response);
    }
}
