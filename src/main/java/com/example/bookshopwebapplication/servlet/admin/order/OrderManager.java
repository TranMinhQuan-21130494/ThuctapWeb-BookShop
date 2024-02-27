package com.example.bookshopwebapplication.servlet.admin.order;

import com.example.bookshopwebapplication.dto.OrderDto;
import com.example.bookshopwebapplication.dto.OrderItemDto;
import com.example.bookshopwebapplication.service.OrderItemService;
import com.example.bookshopwebapplication.service.OrderService;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/orderManager")
public class OrderManager extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final UserService userService = new UserService();
    private final OrderItemService orderItemService = new OrderItemService();

    private static final int ORDERS_PER_PAGE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalOrders = Protector.of(orderService::count).get(0);
        int totalPages = totalOrders / ORDERS_PER_PAGE + (totalOrders % ORDERS_PER_PAGE != 0 ? 1 : 0);

        String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
        int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);
        if (page < 1 || page > totalPages) {
            page = 1;
        }

        int offset = (page - 1) * ORDERS_PER_PAGE;

        List<OrderDto> orders = Protector.of(() -> orderService.getOrderedPart(
                ORDERS_PER_PAGE, offset, "id", "DESC"
        )).get(ArrayList::new);

        for (OrderDto order : orders) {
            Protector.of(() -> userService.getById(order.getUser().getId())).get(Optional::empty).ifPresent(order::setUser);
            List<OrderItemDto> orderItems = Protector.of(() -> orderItemService.getByOrderId(order.getId())).get(ArrayList::new);
            order.setOrderItems(orderItems);
            order.setTotalPrice(calculateTotalPrice(orderItems, order.getDeliveryPrice()));
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("page", page);
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/WEB-INF/views/admin/order/orderManager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    public static double calculateTotalPrice(List<OrderItemDto> orderItems, double deliveryPrice) {
        double totalPrice = deliveryPrice;

        for (OrderItemDto orderItem : orderItems) {
            if (orderItem.getDiscount() == 0) {
                totalPrice += orderItem.getPrice() * orderItem.getQuantity();
            } else {
                totalPrice += (orderItem.getPrice() * (100 - orderItem.getDiscount()) / 100) * orderItem.getQuantity();
            }
        }
        return totalPrice;
    }
}
