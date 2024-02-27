package com.example.bookshopwebapplication.servlet.admin.product;

import com.example.bookshopwebapplication.service.CategoryService;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.utils.Protector;
import com.example.bookshopwebapplication.utils.TextUtils;
import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.dto.CategoryDto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/admin/productManager/detail")
public class ProductDetail extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        Optional<ProductDto> productFromServer = Protector.of(() -> productService.getById(id)).get(Optional::empty);

        if (productFromServer.isPresent()) {
            ProductDto product = productFromServer.get();
            product.setDescription(TextUtils.toParagraph(Optional.ofNullable(product.getDescription()).orElse("")));

            Optional<CategoryDto> categoryFromServer = Protector.of(() -> categoryService.getByProductId(id)).get(Optional::empty);

            request.setAttribute("product", product);
            request.setAttribute("category", categoryFromServer.orElseGet(CategoryDto::new));
            request.getRequestDispatcher("/WEB-INF/views/admin/product/detail.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/productManager");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}
