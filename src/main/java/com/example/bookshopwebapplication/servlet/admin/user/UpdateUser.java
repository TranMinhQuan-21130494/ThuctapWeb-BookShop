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

@WebServlet("/admin/userManager/update")
public class UpdateUser extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<String> id = Optional.ofNullable(request.getParameter("id"));
        if (id.isPresent()) {
            Optional<UserDto> user = Protector.of(() ->
                    userService.getById(Long.parseLong(id.get()))).get(Optional::empty);
            if (user.isPresent()) {
                user.get().setPassword("");
                request.setAttribute("user", user.get());
                request.getRequestDispatcher("/WEB-INF/views/admin/user/update.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/userManager");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/userManager");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserDto user = new UserDto();
        user.setId(Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L));
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setFullName(request.getParameter("fullName"));
        user.setEmail(request.getParameter("email"));
        user.setPhoneNumber(request.getParameter("phoneNumber"));
        user.setGender(Protector.of(() -> Integer.parseInt(request.getParameter("gender"))).get(0));
        user.setAddress(request.getParameter("address"));
        user.setRole(request.getParameter("role"));

        Map<String, List<String>> violations = new HashMap<>();

        Optional<UserDto> userByUsername = Protector.of(() -> userService
                .getByUsername(user.getUsername())).get(Optional::empty);
        Optional<UserDto> userByEmail = Protector.of(() -> userService
                .getByEmail(user.getEmail())).get(Optional::empty);
        Optional<UserDto> userByPhoneNumber = Protector.of(() -> userService
                .getByPhoneNumber(user.getPhoneNumber())).get(Optional::empty);

        violations.put("usernameViolations", Validator.of(user.getUsername())
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(25)
                .isNotExistent(userByUsername.map(UserDto::getId).orElse(user.getId()) != user.getId(),
                        "Tên đăng nhập")
                .toList());
        violations.put("passwordViolations", Validator.of(user.getPassword())
                .isNotBlankAtBothEnds()
                .isAtMostOfLength(32)
                .toList());
        violations.put("fullnameViolations", Validator.of(user.getFullName())
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .toList());
        violations.put("emailViolations", Validator.of(user.getEmail())
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .hasPattern("^[^@]+@[^@]+\\.[^@]+$", "email")
                .isNotExistent(userByEmail.map(UserDto::getId).orElse(user.getId()) != user.getId(),
                        "Email")
                .toList());
        violations.put("phoneNumberViolations", Validator.of(user.getPhoneNumber())
                .isNotNullAndEmpty()
                .isNotBlankAtBothEnds()
                .hasPattern("^\\d{10,11}$", "số điện thoại")
                .isNotExistent(userByPhoneNumber.map(UserDto::getId)
                                .orElse(user.getId()) != user.getId(),
                        "Số điện thoại")
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
        String successMessage = "Sửa thành công!";
        String errorMessage = "Sửa thất bại!";

        if (sumOfViolations == 0) {
            if (user.getPassword().trim().isEmpty()) {
                Optional<UserDto> userUpdate = Protector.of(() ->
                        userService.getById(user.getId())).get(Optional::empty);
                userUpdate.ifPresent(u -> user.setPassword(u.getPassword()));
            } else {
                user.setPassword(EncodePassword.hash(user.getPassword()));
            }
            Protector.of(() -> userService.update(user))
                    .done(r -> {
                        user.setPassword("");
                        request.setAttribute("user", user);
                        request.setAttribute("successMessage", successMessage);
                    })
                    .fail(e -> {
                        user.setPassword("");
                        request.setAttribute("user", user);
                        request.setAttribute("errorMessage", errorMessage);
                    });
        } else {
            request.setAttribute("user", user);
            request.setAttribute("violations", violations);
        }
        request.getRequestDispatcher("/WEB-INF/views/admin/user/update.jsp").forward(request, response);
    }
}
