package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/setting")
public class Setting extends HttpServlet {
    private final UserService userService = new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/client/setting.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy phiên làm việc hiện tại
        HttpSession session = request.getSession();

        // Lấy thông tin người dùng
        UserDto user = (UserDto) session.getAttribute("currentUser");

        // Tạo một bảng lưu trữ các giá trị
        Map<String, String> values = new HashMap<>();
        values.put("username", request.getParameter("username"));
        values.put("fullname", request.getParameter("fullname"));
        values.put("email", request.getParameter("email"));
        values.put("phoneNumber", request.getParameter("phoneNumber"));
        values.put("gender", request.getParameter("gender"));
        values.put("address", request.getParameter("address"));

        // newUser nhận thông tin cập nhật
        UserDto newUser = new UserDto(
                user.getId(),
                values.get("username"),
                user.getPassword(),
                values.get("fullname"),
                values.get("email"),
                values.get("phoneNumber"),
                Integer.parseInt(values.get("gender")),
                values.get("address"),
                "CUSTOMER"
        );

        // Chuỗi thông báo khi cập nhật thành công và khi cập nhật không thành công
        String successMessage = "Cập nhật thành công!";
        String errorMessage = "Cập nhật không thành công!";

        // Kiểm tra xem có người dùng nào trùng tên
        Optional<UserDto> userWithNewUsername = userService.getByUsername(values.get("username"));

        // Nếu tên đăng nhập mới đã được sử dụng
        if (!user.getUsername().equals(values.get("username")) && userWithNewUsername.isPresent()) {
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("user", user);
        } else {
            // Ngược lại, cập nhật thành công
            userService.update(newUser);
            request.setAttribute("successMessage", successMessage);
            request.setAttribute("user", newUser);
            request.getSession().setAttribute("currentUser", newUser);
        }
        request.getRequestDispatcher("WEB-INF/views/client/setting.jsp").forward(request, response);
    }
}