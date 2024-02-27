package com.example.bookshopwebapplication.dao._interface;

import com.example.bookshopwebapplication.entities.ProductReview;

import java.util.List;

public interface IProductReviewDao extends IGenericDao<ProductReview>{
    List<ProductReview> getOrderedPartByProductId(int limit, int offset, String orderBy, String orderDir, long productId);
    int countByProductId(long productId);

    int sumRatingScoresByProductId(long productId);

    void hide(long id);

    void show(long id);
}
