package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.service.CartService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy thông tin người dùng
        UserDto user = (UserDto) request.getSession().getAttribute("currentUser");
        if (user != null) {
            //đếm số lượng item có trong giỏ hàng
            int countCartItemQuantityByUserId = cartService.countCartItemQuantityByUserId(user.getId());
            request.setAttribute("countCartItemQuantity", countCartItemQuantityByUserId);

            //số lượng đơn hàng
            int countOrderByUserId = cartService.countOrderByUserId(user.getId());
            request.setAttribute("countOrder", countOrderByUserId);

            //số lượng đơn hàng đã giao
            int countOrderDeliverByUserId = cartService.countOrderDeliverByUserId(user.getId());
            request.setAttribute("countOrderDeliver", countOrderDeliverByUserId);

            //Đếm số lượng đơn hàng đã nhận
            int countOrderReceivedByUserId = cartService.countOrderReceivedByUserId(user.getId());
            request.setAttribute("countOrderReceived", countOrderReceivedByUserId);
        }
        request.getRequestDispatcher("/WEB-INF/views/client/user.jsp").forward(request, response);
    }
}