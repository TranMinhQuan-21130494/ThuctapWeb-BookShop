package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.ProductDto;
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
import java.util.stream.Collectors;

@WebServlet("/products")
public class AllProduct extends HttpServlet {
    private final CategoryService categoryService = new CategoryService();
    private final ProductService productService = new ProductService();
    private static final int PRODUCTS_PER_PAGE = 9;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Tiêu chí lọc 1: Nhà xuất bản
        Optional<String[]> checkedPublishersParam = Optional.ofNullable(request.getParameterValues("checkedPublishers"));
        List<String> checkedPublishers = !checkedPublishersParam.isPresent() ?
                new LinkedList<>() : Arrays.asList(checkedPublishersParam.get());

        // Tiêu chí lọc 2: Khoảng giá
        Optional<String[]> priceRangesParam = Optional.ofNullable(request.getParameterValues("priceRanges"));
        List<String> priceRanges = !priceRangesParam.isPresent() ?
                new LinkedList<>() : Arrays.asList(priceRangesParam.get());

        // Tiêu chí lọc 3: Thể loại
        Optional<String[]> categoryFilter = Optional.ofNullable(request.getParameterValues("categoryFilter"));
        List<String> categoryFilters = !categoryFilter.isPresent() ? new LinkedList<>() :
                Arrays.asList(categoryFilter.get());

        // Tiêu chí sắp xếp
        Optional<String> orderParam = Optional.ofNullable(request.getParameter("order"));
        String orderBy = orderParam.map(productService::getFirst).orElse("totalBuy");
        String sort = orderParam.map(productService::getLast).orElse("DESC");


        // Tổng hợp các tiêu chí lọc
        List<String> filters = new ArrayList<>();
        categoryFilter.ifPresent(p -> filters.add(productService.filterByCategoryName(categoryFilters)));
        checkedPublishersParam.ifPresent(p -> filters.add(productService.filterByPublishers(checkedPublishers)));
        priceRangesParam.ifPresent(p -> filters.add(productService.filterByPriceRanges(priceRanges)));
        String filtersQuery = !filters.isEmpty() ? productService.createFiltersQuery(filters) : "";

        // Tính tổng số sản phẩm (và có thể là tiêu chí lọc)
        int totalProducts = 0;
        if (!filters.isEmpty()){
            totalProducts = productService.getProductByFilter(filtersQuery).size();
        }else {
            totalProducts =  productService.count();
        }

        // Lấy trang hiện tại, gặp ngoại lệ (chuỗi không phải số, nhỏ hơn 1, lớn hơn tổng số trang)
        // thì gán bằng 1
        String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
        int page = Integer.parseInt(pageParam);
        int totalPages = Paging.totalPages(totalProducts, PRODUCTS_PER_PAGE);
        int offset = Paging.offset(page, totalProducts, PRODUCTS_PER_PAGE);

        // Nếu không có tiêu chí lọc
        List<ProductDto> products = null;
        if (filters.isEmpty()) {
            products = productService.getOrderedPart(PRODUCTS_PER_PAGE, offset, orderBy, sort);
        } else {
            products = productService.getOrderedPartByFilters(PRODUCTS_PER_PAGE, offset, orderBy, sort, filtersQuery);
        }

        // Lấy danh sách nhà xuất bản
        List<String> publishers = productService.getPublishers();
        List<String> categories = categoryService.getAll()
                .stream()
                .map(c -> c.getName()).collect(Collectors.toList());


        if (!filters.isEmpty()) {
            request.setAttribute("filterQueryString",
                    request.getQueryString().replaceAll("&page=\\d{1,5}", ""));
        }
        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("publishers", publishers);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("page", page);
        request.setAttribute("checkedPublishers", checkedPublishers);
        request.setAttribute("priceRanges", priceRanges);
        request.setAttribute("categoryFilters", categoryFilters);
        request.setAttribute("order", orderParam.orElse("totalBuy-DESC"));
        String res = request.getQueryString();
        if (!filters.isEmpty()) {
            request.setAttribute("filterQueryString",
                    request.getQueryString().replaceAll("&page=\\d{1,5}", ""));
        }
        request.getRequestDispatcher("/WEB-INF/views/client/allProductFilter.jsp").forward(request, response);
    }
}
