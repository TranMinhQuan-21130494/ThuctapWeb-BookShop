package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dao.ProductDao;
import com.example.bookshopwebapplication.dto.CategoryDto;
import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.entities.Category;
import com.example.bookshopwebapplication.entities.Product;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.service.CategoryService;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.EncodePassword;
import com.example.bookshopwebapplication.utils.Paging;
import com.example.bookshopwebapplication.utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "HomeClient", value = {""})
public class Home extends HttpServlet {
    private CategoryService categoryService = new CategoryService();
    private ProductService productService = new ProductService();
    private static final int CATEGORY_PER_PAGE = 12;
    private static final int PRODUCT_PER_PAGE = 16;
    private int total;
    private int page;
    private int totalPage;
    private int offset;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy danh sách danh mục sản phẩm
        List<CategoryDto> categories = categoryService.getPart(12, 0);

        // Lấy danh sách sản phẩm được sắp xếp theo thời gian tạo giảm dần
        List<ProductDto> products = productService.getOrderedPart(12, 0, "createdAt", "DESC");

        Optional<String> showCategories = Optional.ofNullable(request.getParameter("category"));
        Optional<String> showProducts = Optional.ofNullable(request.getParameter("product"));
        Optional<String> newProducts = Optional.ofNullable(request.getParameter("new"));
        Optional<String> sellingProduct = Optional.ofNullable(request.getParameter("selling"));
        Optional<String> promotion = Optional.ofNullable(request.getParameter("promotion"));
        if (showCategories.isPresent() && showCategories.get().equals("all")) {

            initVar(categoryService.count(), request, CATEGORY_PER_PAGE);
            categories = categoryService.getPart(CATEGORY_PER_PAGE, offset);

            request.setAttribute("categories", categories);
            request.setAttribute("totalPages", totalPage);
            request.setAttribute("page", page);
            request.getRequestDispatcher("/WEB-INF/views/client/allCategory.jsp").forward(request, response);
            return;
        }
        if (showProducts.isPresent() && showProducts.get().equals("all")) {
            initVar(productService.count(), request, PRODUCT_PER_PAGE);

            if (newProducts.isPresent() && newProducts.get().equals("newProduct")) {
                products = productService.getOrderedPart(PRODUCT_PER_PAGE, offset, "createdAt", "DESC");

            } else if (sellingProduct.isPresent() && sellingProduct.get().equals("sell")) {
                products = productService.getOrderedPart(PRODUCT_PER_PAGE, offset, "totalBuy", "DESC");
            } else if (promotion.isPresent() && promotion.get().equals("promo")) {
                products = productService.getOrderedPart(PRODUCT_PER_PAGE, offset, "discount", "DESC");
            } else {
                products = productService.getPart(PRODUCT_PER_PAGE, offset);
            }
            request.setAttribute("products", products);
            request.setAttribute("totalPages", totalPage);
            request.setAttribute("page", page);
            request.getRequestDispatcher("/WEB-INF/views/client/allProduct.jsp").forward(request, response);
            return;
        }

        // Đặt danh sách danh mục và sản phẩm vào request để sử dụng trong trang JSP
        request.setAttribute("categories", categories);
        request.setAttribute("products", products);

        // Chuyển hướng người dùng đến trang chủ
        request.getRequestDispatcher("/WEB-INF/views/client/home.jsp").forward(request, response);
    }

    private void initVar(int totalItems, HttpServletRequest request, int show) {
        String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
        page = Integer.parseInt(pageParam);

        totalPage = Paging.totalPages(totalItems, show);
        offset = Paging.offset(page, totalItems, show);
    }
}
