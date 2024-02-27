package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.service.ProductService;
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

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    private static final int PRODUCTS_PER_PAGE = 12;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<String> query = Optional.ofNullable(request.getParameter("q")).filter(s -> !s.trim().isEmpty());

        if (query.isPresent()) {
            String queryStr = query.get();

            int totalProducts = Protector.of(() -> productService.countByQuery(queryStr)).get(0);
            String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
            int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);

            int totalPages = Paging.totalPages(totalProducts, PRODUCTS_PER_PAGE);
            int offset = Paging.offset(page, totalProducts, PRODUCTS_PER_PAGE);

            List<ProductDto> products = Protector.of(() -> productService.getByQuery(
                    queryStr, PRODUCTS_PER_PAGE, offset
            )).get(ArrayList::new);

            products.forEach(productDto -> productDto.setName(productDto.getName()
                    .replaceAll("(?i)(" + queryStr + ")", "<b class='bg-warning'>$1</b>")));

            request.setAttribute("query", queryStr);
            request.setAttribute("totalProducts", totalProducts);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("page", page);
            request.setAttribute("products", products);
            request.getRequestDispatcher("/WEB-INF/views/client/searchView.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
