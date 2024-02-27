package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.ProductDao;
import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.entities.Product;
import com.example.bookshopwebapplication.service._interface.IProductService;
import com.example.bookshopwebapplication.service.transferObject.TProduct;

import java.util.*;
import java.util.stream.Collectors;


public class ProductService implements IProductService {

    private ProductDao productDao = new ProductDao();

    private TProduct tProduct = new TProduct();

    private static final ProductService instance = new ProductService();

    public static ProductService getInstance() {
        return instance;
    }

    // Phương thức để chèn một đối tượng ProductDto mới
    @Override
    public Optional<ProductDto> insert(ProductDto productDto) {
        Long id = productDao.save(tProduct.toEntity(productDto));
        return getById(id);
    }

    // Phương thức để cập nhật thông tin của một đối tượng ProductDto
    @Override
    public Optional<ProductDto> update(ProductDto productDto) {
        productDao.update(tProduct.toEntity(productDto));
        return getById(productDto.getId());
    }

    // Phương thức để xóa các đối tượng Product theo danh sách các id
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            productDao.delete(id);
        }
    }

    // Phương thức để lấy đối tượng ProductDto dựa trên id
    @Override
    public Optional<ProductDto> getById(Long id) {
        Optional<Product> product = productDao.getById(id);
        if (product.isPresent()) return Optional.of(tProduct.toDto(product.get()));
        return Optional.empty();
    }

    // Phương thức để lấy một phần của danh sách đối tượng ProductDto
    @Override
    public List<ProductDto> getPart(Integer limit, Integer offset) {
        return productDao.getPart(limit, offset)
                .stream()
                .map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    // Phương thức để lấy một phần của danh sách đối tượng ProductDto và sắp xếp theo thứ tự
    @Override
    public List<ProductDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return productDao.getOrderedPart(limit, offset, orderBy, sort)
                .stream()
                .map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    // Phương thức để đếm số lượng sản phẩm theo categoryId
    @Override
    public int count() {
        return productDao.count();
    }

    @Override
    public int countByCategoryId(long categoryId) {
        return productDao.countByCategoryId(categoryId);
    }

    // Phương thức để lấy một phần của danh sách đối tượng ProductDto và sắp xếp theo thứ tự, dựa trên categoryId
    @Override
    public List<ProductDto> getOrderedPartByCategoryId(int limit, int offset, String orderBy, String sort, Long id) {
        return productDao.getOrderedPartByCategoryId(limit, offset, orderBy, sort, id)
                .stream()
                .map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    // Phương thức để lấy một phần của danh sách đối tượng ProductDto và áp dụng bộ lọc, sắp xếp theo thứ tự, dựa trên categoryId
    @Override
    public List<ProductDto> getOrderedPartByCategoryIdAndFilters(int limit, int offset, String orderBy, String sort, Long id, String filtersQuery) {
        return productDao.getOrderedPartByCategoryIdAndFilters(limit, offset, orderBy, sort, id, filtersQuery)
                .stream()
                .map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getOrderedPartByFilters(int limit, int offset, String orderBy, String sort, String filters) {
        return productDao.getOrderedPartByFilters(limit, offset, orderBy, sort, filters)
                .stream()
                .map(p -> tProduct.toDto(p))
                .collect(Collectors.toList());
    }

    // Phương thức để lấy danh sách các nhà xuất bản dựa trên categoryId
    @Override
    public List<String> getPublishersByCategoryId(Long categoryId) {
        return productDao.getPublishersByCategoryId(categoryId);
    }

    // Phương thức để lấy một phần ngẫu nhiên của danh sách đối tượng ProductDto dựa trên categoryId
    @Override
    public List<ProductDto> getRandomPartByCategoryId(int limit, int offset, Long categoryId) {
        return productDao.getRandomPartByCategoryId(limit, offset, categoryId)
                .stream().map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    // Phương thức để lấy danh sách đối tượng ProductDto dựa trên categoryId
    @Override
    public List<ProductDto> getProductsByCategoryId(Long categoryId) {
        return productDao.getProductsByCategoryId(categoryId)
                .stream().map(product -> tProduct.toDto(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getPublishers() {
        return productDao.getPublishers();
    }

    // Phương thức để lấy phần đầu tiên của một chuỗi hai phần, ví dụ: "ABC - XYZ" -> "ABC"
    @Override
    public int countByQuery(String query) {
        return productDao.countByQuery(query);
    }

    @Override
    public List<ProductDto> getByQuery(String query, int limit, int offset) {
        return productDao.getByQuery(query, limit, offset)
                .stream()
                .map(product -> getById(product.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toList());
    }

    @Override
    public void insertProductCategory(long productId, long categoryId) {
        productDao.insertProductCategory(productId, categoryId);
    }

    @Override
    public void updateProductCategory(long productId, long categoryId) {
        productDao.updateProductCategory(productId, categoryId);
    }

    @Override
    public void deleteProductCategory(long productId, long categoryId) {
        productDao.deleteProductCategory(productId, categoryId);
    }

    @Override
    public String getFirst(String twopartString) {
        return twopartString.contains("-") ? twopartString.split("-")[0] : "";
    }

    // Phương thức để lấy phần cuối cùng của một chuỗi hai phần, ví dụ: "ABC - XYZ" -> "XYZ"
    @Override
    public String getLast(String twopartString) {
        return twopartString.contains("-") ? twopartString.split("-")[1] : "";
    }

    // Phương thức để lấy giá trị số nhỏ nhất từ một phạm vi giá
    private int getMinPrice(String priceRange) {
        return Integer.parseInt(getFirst(priceRange));
    }

    // Phương thức để lấy giá trị số lớn nhất từ một phạm vi giá
    private int getMaxPrice(String priceRange) {
        String maxPriceString = getLast(priceRange);
        if (maxPriceString.equals("infinity")) {
            return Integer.MAX_VALUE;
        }
        return Integer.parseInt(maxPriceString);
    }

    // Phương thức để tạo điều kiện lọc theo nhà xuất bản
    @Override
    public String filterByPublishers(List<String> publishers) {
        String publishersString = publishers.stream().map(p -> "'" + p + "'").collect(Collectors.joining(", "));
        return "p.publisher IN (" + publishersString + ")";
    }

    // Phương thức để tạo điều kiện lọc theo phạm vi giá
    @Override
    public String filterByPriceRanges(List<String> priceRanges) {
        String priceRangeConditions = priceRanges.stream().map(
                priceRange -> "p.price BETWEEN " + getMinPrice(priceRange) + " AND " + getMaxPrice(priceRange)
        ).collect(Collectors.joining(" OR "));
        return "(" + priceRangeConditions + ")";
    }

    @Override
    public String filterByCategoryName(List<String> categoriesName) {
        String categoryNames = categoriesName
                .stream()
                .map(c -> "'" + c + "'")
                .collect(Collectors.joining(", "));
        return "p.id IN (" + productDao.getIDByCategoriesName("(" + categoryNames + ")") + ")";
    }

    @Override
    public List<ProductDto> getProductByFilter(String filters) {
        productDao.getProductByFilter(filters);
        return productDao.getProductByFilter(filters)
                .stream()
                .map(p -> tProduct.toDto(p))
                .collect(Collectors.toList());
    }

    // Phương thức để tạo chuỗi truy vấn từ danh sách điều kiện lọc
    @Override
    public String createFiltersQuery(List<String> filters) {
        return String.join(" AND ", filters);
    }

    // Phương thức để đếm số lượng sản phẩm dựa trên categoryId và điều kiện lọc
    public int countByCategoryIdAndFilters(Long id, String filtersQuery) {
        return productDao.countByCategoryIdAndFilters(id, filtersQuery);
    }
}
