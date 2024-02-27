package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.OrderDto;
import com.example.bookshopwebapplication.dto.OrderItemDto;
import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.network.OrderItemRequest;
import com.example.bookshopwebapplication.network.OrderRequest;
import com.example.bookshopwebapplication.entities.Order;
import com.example.bookshopwebapplication.entities.OrderItem;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.message.Message;
import com.example.bookshopwebapplication.service.CartService;
import com.example.bookshopwebapplication.service.OrderItemService;
import com.example.bookshopwebapplication.service.OrderService;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.utils.JsonUtils;
import com.example.bookshopwebapplication.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    private final OrderItemService orderItemService = new OrderItemService();
    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("WEB-INF/views/client/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy đối tượng orderRequest từ JSON trong request
        OrderRequest orderRequest = JsonUtils.get(request, OrderRequest.class);

        // Tạo order
        OrderDto order = new OrderDto();
        order.setUser((UserDto) request.getSession().getAttribute("currentUser"));
        order.setStatus(1);
        order.setDeliveryMethod(orderRequest.getDeliveryMethod());
        order.setDeliveryPrice(orderRequest.getDeliveryPrice());
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        long orderId = orderService.insert(order).get().getId();

        String successMessage = "Đã đặt hàng và tạo đơn hàng thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new Message(200, successMessage),
                HttpServletResponse.SC_OK);
        Runnable failFunction = () -> JsonUtils.out(
                response,
                new Message(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        if (orderId > 0L) {
            List<OrderItemDto> orderItems = new ArrayList<>();
            for (OrderItemRequest orderItemRequest: orderRequest.getOrderItems()){
                OrderItemDto orderItem = new OrderItemDto();
                orderItem.setPrice(orderItemRequest.getPrice());
                orderItem.setDiscount(orderItemRequest.getDiscount());
                orderItem.setQuantity(orderItemRequest.getQuantity());
                orderItem.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                orderItem.setOrder(orderService.getById(orderId).get());
                orderItem.setProduct(productService.getById(orderItemRequest.getProductId()).get());

                orderItems.add(orderItem);
            }
            Protector.of(() -> {
                        orderItemService.bulkInsert(orderItems);
                        cartService.delete(new Long[]{orderRequest.getCartId()});
                    })
                    .done(r -> doneFunction.run())
                    .fail(e -> failFunction.run());
        } else {
            failFunction.run();
        }
    }
}
