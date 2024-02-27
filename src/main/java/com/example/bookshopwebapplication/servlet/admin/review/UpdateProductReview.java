package com.example.bookshopwebapplication.servlet.admin.review;

import com.example.bookshopwebapplication.service.ProductReviewService;
import com.example.bookshopwebapplication.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/reviewManager/update")
public class UpdateProductReview extends HttpServlet {
    private final ProductReviewService productReviewService = new ProductReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        String action = request.getParameter("action");

        String errorMessage = "Đã có lỗi truy vấn!";

        if ("HIDE".equals(action)) {
            String successMessage = String.format("Đã ẩn đánh giá #%s thành công!", id);
            Protector.of(() -> productReviewService.hide(id))
                    .done(r -> request.getSession().setAttribute("successMessage", successMessage))
                    .fail(e -> request.getSession().setAttribute("errorMessage", errorMessage));
        }

        if ("SHOW".equals(action)) {
            String successMessage = String.format("Đã hiện đánh giá #%s thành công!", id);
            Protector.of(() -> productReviewService.show(id))
                    .done(r -> request.getSession().setAttribute("successMessage", successMessage))
                    .fail(e -> request.getSession().setAttribute("errorMessage", errorMessage));
        }

        response.sendRedirect(request.getContextPath() + "/admin/reviewManager");
    }
}
