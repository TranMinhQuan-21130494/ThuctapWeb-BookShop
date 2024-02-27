package com.example.bookshopwebapplication.servlet.admin.category;

import com.example.bookshopwebapplication.dto.CategoryDto;
import com.example.bookshopwebapplication.service.CategoryService;
import com.example.bookshopwebapplication.utils.ImageUtils;
import com.example.bookshopwebapplication.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/admin/categoryManager/delete")
public class DeleteCategory extends HttpServlet {
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<CategoryDto> categoryFromServer = Protector.of(() -> categoryService.getById(id)).get(Optional::empty);
        String successMessage = String.format("Xóa thể loại #%s thành công!", id);
        String errorMessage = String.format("Xóa thể loại #%s thất bại!", id);
        Protector.of(() -> {
                    Optional.ofNullable(categoryFromServer.get()
                            .getImageName()).ifPresent(s -> ImageUtils.delete(s, request));
                    categoryService.delete(new Long[]{id});
                })
                .done(r -> request.getSession().setAttribute("successMessage", successMessage))
                .fail(e -> request.getSession().setAttribute("errorMessage", errorMessage));
        response.sendRedirect(request.getContextPath() + "/admin/categoryManager");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
