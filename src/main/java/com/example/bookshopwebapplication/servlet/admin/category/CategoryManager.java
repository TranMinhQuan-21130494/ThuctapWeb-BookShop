package com.example.bookshopwebapplication.servlet.admin.category;

import com.example.bookshopwebapplication.dto.CategoryDto;
import com.example.bookshopwebapplication.service.CategoryService;
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

@WebServlet("/admin/categoryManager")
public class CategoryManager extends HttpServlet {
    private final CategoryService categoryService = new CategoryService();

    private static final int CATEGORIES_PER_PAGE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalCategories = Protector.of(categoryService::count).get(0);

        String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
        int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);

        int totalPages = Paging.totalPages(totalCategories, CATEGORIES_PER_PAGE);
        int offset = Paging.offset(page, totalCategories, CATEGORIES_PER_PAGE);

        List<CategoryDto> categories = Protector.of(() -> categoryService.getOrderedPart(
                CATEGORIES_PER_PAGE, offset, "id", "DESC"
        )).get(ArrayList::new);

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("page", page);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/admin/category/categoryManager.jsp").forward(request, response);
    }
}
