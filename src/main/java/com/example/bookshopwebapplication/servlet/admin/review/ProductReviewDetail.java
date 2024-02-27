package com.example.bookshopwebapplication.servlet.admin.review;

import com.example.bookshopwebapplication.dto.ProductReviewDto;
import com.example.bookshopwebapplication.service.ProductReviewService;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.Protector;
import com.example.bookshopwebapplication.utils.TextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/admin/reviewManager/detail")
public class ProductReviewDetail extends HttpServlet {
    private final ProductReviewService productReviewService = new ProductReviewService();
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<ProductReviewDto> productReviewFromServer = Protector.of(() -> productReviewService.getById(id))
                .get(Optional::empty);

        if (productReviewFromServer.isPresent()) {
            ProductReviewDto productReview = productReviewFromServer.get();
            productReview.setContent(TextUtils.toParagraph(productReview.getContent()));
            Protector.of(() -> userService.getById(productReview.getUser().getId())).get(Optional::empty)
                    .ifPresent(productReview::setUser);
            Protector.of(() -> productService.getById(productReview.getProduct().getId())).get(Optional::empty)
                    .ifPresent(productReview::setProduct);
            request.setAttribute("productReview", productReview);
            request.getRequestDispatcher("/WEB-INF/views/admin/review/detail.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/reviewManager");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
