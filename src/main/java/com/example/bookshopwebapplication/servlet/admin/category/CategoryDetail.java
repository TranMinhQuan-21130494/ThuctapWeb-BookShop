package com.example.bookshopwebapplication.servlet.admin.category;

import com.example.bookshopwebapplication.dto.CategoryDto;
import com.example.bookshopwebapplication.service.CategoryService;
import com.example.bookshopwebapplication.utils.Protector;
import com.example.bookshopwebapplication.utils.TextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/admin/categoryManager/detail")
public class CategoryDetail extends HttpServlet {
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<CategoryDto> categoryFromServer = Protector.of(() -> categoryService.getById(id)).get(Optional::empty);

        if (categoryFromServer.isPresent()) {
            CategoryDto category = categoryFromServer.get();
            category.setDescription(TextUtils.toParagraph(Optional.ofNullable(category.getDescription()).orElse("")));
            request.setAttribute("category", category);
            request.getRequestDispatcher("/WEB-INF/views/admin/category/detail.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/categoryManager");
        }
    }

}
