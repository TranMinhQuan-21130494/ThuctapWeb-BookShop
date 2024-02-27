package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.*;
import com.example.bookshopwebapplication.message.Message;
import com.example.bookshopwebapplication.network.OrderItemRequest;
import com.example.bookshopwebapplication.network.OrderProductRequest;
import com.example.bookshopwebapplication.network.OrderRequest;
import com.example.bookshopwebapplication.service.*;
import com.example.bookshopwebapplication.utils.JsonUtils;
import com.example.bookshopwebapplication.utils.Protector;
import com.example.bookshopwebapplication.utils.TextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/checkout")
public class Checkout extends HttpServlet {
    private final OrderService orderService = OrderService.getInstance();
    private final OrderItemService orderItemService = OrderItemService.getInstance();
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();
    private final ProductReviewService productReviewService = new ProductReviewService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        Optional<ProductDto> productFromServer = productService.getById(id);
        if (id > 0L && productFromServer.isPresent()) {
            Optional<CategoryDto> categoryFromServer = categoryService.getByProductId(id);
            CategoryDto category = categoryFromServer.orElseGet(CategoryDto::new);

            // Lấy product từ productFromServer
            ProductDto product = productFromServer.get();
            product.setDescription(TextUtils.toParagraph(
                    Optional.ofNullable(product.getDescription()).orElse(""))
            );

            // Lấy tổng số đánh giá (productReview) của sản phẩm
            int totalProductReviews = productReviewService.countByProductId(id);


            // Lấy tổng cộng số sao đánh giá của sản phẩm
            int sumRatingScores = productReviewService.sumRatingScoresByProductId(id);

            // Tính số sao đánh giá trung bình
            int averageRatingScore = (totalProductReviews == 0) ? 0 : (sumRatingScores / totalProductReviews);

            request.setAttribute("category", category);
            request.setAttribute("quantity", quantity);
            request.setAttribute("product", product);
            request.setAttribute("totalProductReviews", totalProductReviews);
            request.setAttribute("averageRatingScore", averageRatingScore);
            request.getRequestDispatcher("/WEB-INF/views/client/productBuyNow.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderProductRequest orderProductRequest = JsonUtils.get(request, OrderProductRequest.class);
        if (orderProductRequest != null){
            OrderDto order = new OrderDto();
            order.setUser((UserDto) request.getSession().getAttribute("currentUser"));
            order.setStatus(1);
            order.setDeliveryPrice(orderProductRequest.getDeliveryPrice());
            order.setDeliveryMethod(orderProductRequest.getDeliveryMethod());
            order.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            Optional<OrderDto> orderInserted = Protector.of(() -> orderService.insert(order)).get(Optional::empty);
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

            if (orderInserted.get().getId() > 0L) {
                ProductDto product = productService.getById(orderProductRequest.getProductId()).get();
                List<OrderItemDto> orderItems = new ArrayList<>();
                orderItems.add(new OrderItemDto(
                        product.getPrice(),
                        product.getDiscount(),
                        orderProductRequest.getQuantity(),
                        new Timestamp(System.currentTimeMillis()),
                        null,
                        orderInserted.get(),
                        product
                ));
                Protector.of(() -> orderItemService.bulkInsert(orderItems))
                        .done(r -> doneFunction.run())
                        .fail(e -> failFunction.run());
            } else {
                failFunction.run();
            }
        }
    }
}
