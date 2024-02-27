package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.dto.ProductReviewDto;
import com.example.bookshopwebapplication.entities.ProductReview;

import java.util.List;

public interface IProductReviewService extends IService<ProductReviewDto>{
    public List<ProductReviewDto> getOrderedPartByProductId(int limit, int offset, String orderBy, String orderDir, long productId);
    int countByProductId(long id);

    int sumRatingScoresByProductId(long id);
}
