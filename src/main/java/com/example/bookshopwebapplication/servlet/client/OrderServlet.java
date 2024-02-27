package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.OrderDto;
import com.example.bookshopwebapplication.dto.OrderItemDto;
import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.network.OrderResponse;
import com.example.bookshopwebapplication.entities.Order;
import com.example.bookshopwebapplication.entities.OrderItem;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.service.OrderItemService;
import com.example.bookshopwebapplication.service.OrderService;
import com.example.bookshopwebapplication.utils.Paging;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final OrderItemService orderItemService = new OrderItemService();
    private static final int ORDERS_PER_PAGE = 3;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user = (UserDto) request.getSession().getAttribute("currentUser");
        System.out.println(request.getSession().getId());
        if (user != null) {
            int totalOrders = orderService.countByUserId(user.getId());

            // Lấy trang hiện tại, gặp ngoại lệ (chuỗi không phải số, nhỏ hơn 1, lớn hơn tổng số trang) thì gán bằng 1
            String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
            int page = Integer.parseInt(pageParam);

            // Tính tổng số trang (= tổng số total / số sản phẩm trên mỗi trang)
            int totalPages = Paging.totalPages(totalOrders, ORDERS_PER_PAGE);

            // Tính mốc truy vấn (offset)
            int offset = Paging.offset(page, totalOrders, ORDERS_PER_PAGE);

            // Lấy danh sách order, lấy với số lượng là ORDERS_PER_PAGE và tính từ mốc offset
            List<OrderDto> orders = orderService.getOrderedPartByUserId(
                    user.getId(), ORDERS_PER_PAGE, offset
            );

            // Tạo response order để show
            List<OrderResponse> orderResponses = new ArrayList<>();
            for (OrderDto order : orders.isEmpty() ? new LinkedList<OrderDto>() : orders) {
                // set orderItems for Order
                order.setOrderItems(orderItemService.getByOrderId(order.getId()));

                double totalPrice = orderService.totalPrice(order);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
                OrderResponse orderResponse = new OrderResponse(
                        order.getId(),
                        simpleDateFormat.format(order.getCreatedAt()),
                        orderItemService.getProductNamesByOrderId(order.getId()),
                        order.getStatus(),
                        totalPrice + order.getDeliveryPrice());

                orderResponses.add(orderResponse);
            }
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("page", page);
            request.setAttribute("orders", orderResponses);
        }

        request.getRequestDispatcher("/WEB-INF/views/client/myOrder.jsp").forward(request, response);
    }
}
