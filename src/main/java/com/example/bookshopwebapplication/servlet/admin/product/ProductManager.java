package com.example.bookshopwebapplication.servlet.admin.product;

import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.utils.Paging;
import com.example.bookshopwebapplication.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/productManager")
public class ProductManager extends HttpServlet {
    private final ProductService productService = new ProductService();

    private static final int PRODUCTS_PER_PAGE = 15;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalProducts = Protector.of(productService::count).get(0);
        String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
        int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);

        int totalPages = Paging.totalPages(totalProducts,PRODUCTS_PER_PAGE);
        int offset = Paging.offset(page, totalProducts, PRODUCTS_PER_PAGE);

        List<ProductDto> products = Protector.of(() -> productService.getOrderedPart(
                PRODUCTS_PER_PAGE, offset, "id", "DESC"
        )).get(ArrayList::new);

        request.setAttribute("totalPages", totalPages);
        request.setAttribute("page", page);
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/views/admin/product/productManager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
