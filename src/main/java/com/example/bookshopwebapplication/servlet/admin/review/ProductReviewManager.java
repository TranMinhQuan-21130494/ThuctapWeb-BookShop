package com.example.bookshopwebapplication.servlet.admin.review;

import com.example.bookshopwebapplication.dto.ProductReviewDto;
import com.example.bookshopwebapplication.service.ProductReviewService;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.Paging;
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

@WebServlet("/admin/reviewManager")
public class ProductReviewManager extends HttpServlet {
    private final ProductReviewService productReviewService = new ProductReviewService();
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();

    private static final int PRODUCT_REVIEWS_PER_PAGE = 25;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalProductReviews = Protector.of(productReviewService::count).get(0);
        String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
        int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);

        int totalPages = Paging.totalPages(totalProductReviews, PRODUCT_REVIEWS_PER_PAGE);
        int offset = Paging.offset(page, totalProductReviews, PRODUCT_REVIEWS_PER_PAGE);

        List<ProductReviewDto> productReviews = Protector.of(() -> productReviewService.getOrderedPart(
                PRODUCT_REVIEWS_PER_PAGE, offset, "id", "DESC"
        )).get(ArrayList::new);

        for (ProductReviewDto productReview : productReviews) {
            Protector.of(() -> userService.getById(productReview.getUser().getId())).get(Optional::empty)
                    .ifPresent(productReview::setUser);
            Protector.of(() -> productService.getById(productReview.getProduct().getId())).get(Optional::empty)
                    .ifPresent(productReview::setProduct);
        }

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("page", page);
        request.setAttribute("productReviews", productReviews);
        request.getRequestDispatcher("/WEB-INF/views/admin/review/productReviewManager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
