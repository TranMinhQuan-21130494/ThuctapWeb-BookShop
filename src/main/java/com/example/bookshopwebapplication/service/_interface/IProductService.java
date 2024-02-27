package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.entities.Product;

import java.util.List;

public interface IProductService extends IService<ProductDto> {
    int countByCategoryId(long categoryId);

    public String getFirst(String twopartString);

    public String getLast(String twopartString);

    public String filterByPublishers(List<String> publishers);

    public String filterByPriceRanges(List<String> priceRanges);

    String filterByCategoryName(List<String> categoriesName);

    List<ProductDto> getProductByFilter(String filters);

    public String createFiltersQuery(List<String> filters);

    List<ProductDto> getOrderedPartByCategoryId(int productsPerPage, int offset, String orderBy, String sort, Long id);

    List<ProductDto> getOrderedPartByCategoryIdAndFilters(int productsPerPage, int offset, String orderBy, String sort, Long id, String filtersQuery);

    List<ProductDto> getOrderedPartByFilters(int limit, int offset, String orderBy, String sort, String filters);

    List<String> getPublishersByCategoryId(Long id);

    List<ProductDto> getRandomPartByCategoryId(int limit, int offset, Long id);
    List<ProductDto> getProductsByCategoryId(Long categoryId);
    List<String> getPublishers();
    int countByQuery(String queryStr);

    List<ProductDto> getByQuery(String query, int limit, int offset);
    void insertProductCategory(long productId, long categoryId);

    void updateProductCategory(long productId, long categoryId);

    void deleteProductCategory(long productId, long categoryId);
}
