package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.EncodePassword;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/changePassword")
public class ChangePassword extends HttpServlet {
    private final UserService userService = new UserService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/client/changePassword.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto userFromServer = (UserDto) request.getSession().getAttribute("currentUser");

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (userFromServer != null) {
            Map<String, String> values = new HashMap<>();
            values.put("currentPassword", request.getParameter("currentPassword"));
            values.put("newPassword", request.getParameter("newPassword"));
            values.put("newPasswordAgain", request.getParameter("newPasswordAgain"));

            // Kiểm tra xem mật khẩu hiện tại có khớp với mật khẩu cũ hay không
            boolean currentPasswordEqualsUserPassword = EncodePassword.hash(values.get("currentPassword")).equals(userFromServer.getPassword());
            boolean newPasswordEqualsNewPasswordAgain = values.get("newPassword").equals(values.get("newPasswordAgain"));

            //thực hiện thay đổi mật khẩu
            if (currentPasswordEqualsUserPassword && newPasswordEqualsNewPasswordAgain) {
                // Mã hóa mật khẩu mới
                String newPassword = EncodePassword.hash(values.get("newPassword"));
                // Thực hiện thay đổi mật khẩu trong cơ sở dữ liệu
                userService.changePassword(userFromServer.getId(), newPassword);
                // Hiển thị thông báo thành công
                String successMessage = "Đổi mật khẩu thành công!";
                request.setAttribute("successMessage", successMessage);
            } else {
                // Hiển thị thông báo lỗi nếu điều kiện không đúng
                String errorMessage = "Đổi mật khẩu thất bại!";
                request.setAttribute("errorMessage", errorMessage);
            }
            // Chuyển hướng người dùng đến trang đổi mật khẩu với thông báo tương ứng
            request.getRequestDispatcher("/WEB-INF/views/client/changePassword.jsp").forward(request, response);
        }
    }
}
