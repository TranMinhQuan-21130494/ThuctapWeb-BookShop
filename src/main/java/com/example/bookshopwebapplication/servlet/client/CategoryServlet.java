package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.CategoryDto;
import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.entities.Category;
import com.example.bookshopwebapplication.entities.Product;
import com.example.bookshopwebapplication.service.CategoryService;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.utils.Paging;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/category")
public class CategoryServlet extends HttpServlet {
    private final CategoryService categoryService = new CategoryService();
    private final ProductService productService = new ProductService();
    private static final int PRODUCTS_PER_PAGE = 6;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        Optional<CategoryDto> category = categoryService.getById(id);
        if (id > 0L && category.isPresent()) {
            // Tiêu chí lọc 1: Nhà xuất bản
            Optional<String[]> checkedPublishersParam = Optional.ofNullable(req.getParameterValues("checkedPublishers"));
            List<String> checkedPublishers = !checkedPublishersParam.isPresent() ?
                    new LinkedList<String>() : Arrays.asList(checkedPublishersParam.get());

            // Tiêu chí lọc 2: Khoảng giá
            Optional<String[]> priceRangesParam = Optional.ofNullable(req.getParameterValues("priceRanges"));
            List<String> priceRanges = !priceRangesParam.isPresent() ?
                    new LinkedList<>() : Arrays.asList(priceRangesParam.get());

            // Tiêu chí sắp xếp
            Optional<String> orderParam = Optional.ofNullable(req.getParameter("order"));
            String orderBy = orderParam.map(productService::getFirst).orElse("totalBuy");
            String sort = orderParam.map(productService::getLast).orElse("DESC");

            // Tổng hợp các tiêu chí lọc
            List<String> filters = new ArrayList<>();
            checkedPublishersParam.ifPresent(p -> filters.add(productService.filterByPublishers(checkedPublishers)));
            priceRangesParam.ifPresent(p -> filters.add(productService.filterByPriceRanges(priceRanges)));
            String filtersQuery = productService.createFiltersQuery(filters);

            // Tính tổng số sản phẩm của thể loại (và có thể là tiêu chí lọc)
            int totalProducts;

            // Nếu không có tiêu chí lọc
            if (filters.isEmpty()) {
                totalProducts = productService.countByCategoryId(id);
            } else {
                totalProducts = productService.countByCategoryIdAndFilters(id, filtersQuery);
            }

            // Lấy trang hiện tại, gặp ngoại lệ (chuỗi không phải số, nhỏ hơn 1, lớn hơn tổng số trang)
            // thì gán bằng 1
            String pageParam = Optional.ofNullable(req.getParameter("page")).orElse("1");
            int page = Integer.parseInt(pageParam);

            // Tính tổng page
            int totalPages = Paging.totalPages(totalProducts,PRODUCTS_PER_PAGE);

            // Tính mốc truy vấn (offset)
            int offset = Paging.offset(page, totalProducts, PRODUCTS_PER_PAGE);

            // Nếu không có tiêu chí lọc
            if (filters.isEmpty()) {
                category.get().setProducts(productService.getOrderedPartByCategoryId(
                        PRODUCTS_PER_PAGE, offset, orderBy, sort, id
                ));
            } else {
                category.get().setProducts(productService.getOrderedPartByCategoryIdAndFilters(
                        PRODUCTS_PER_PAGE, offset, orderBy, sort, id, filtersQuery
                ));
            }

            // Lấy danh sách nhà xuất bản (tiêu chí lọc 1)
            List<String> publishers = productService.getPublishersByCategoryId(id);

            req.setAttribute("category", category.get());
            req.setAttribute("totalProducts", totalProducts);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("page", page);
            req.setAttribute("publishers", publishers);
            req.setAttribute("checkedPublishers", checkedPublishers);
            req.setAttribute("priceRanges", priceRanges);
            req.setAttribute("order", orderParam.orElse("totalBuy-DESC"));
            req.setAttribute("filterQueryString",
                    req.getQueryString().replaceAll("^id=\\d{1,5}(&page=\\d{1,5}|)", ""));
            req.getRequestDispatcher("/WEB-INF/views/client/categoryView.jsp").forward(req, resp);
        } else {
            // Nếu id không phải là số nguyên hoặc không hiện diện trong bảng category
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}