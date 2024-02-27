package com.example.bookshopwebapplication.dao.mapper;

import com.example.bookshopwebapplication.dao.mapper._interface.IRowMapper;
import com.example.bookshopwebapplication.entities.ProductReview;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductReviewMapper implements IRowMapper<ProductReview> {
    @Override
    public ProductReview mapRow(ResultSet resultSet) {
        try {
            ProductReview productReview = new ProductReview();
            productReview.setId(resultSet.getLong("id"));
            productReview.setUserId(resultSet.getLong("userId"));
            productReview.setProductId(resultSet.getLong("productId"));
            productReview.setRatingScore(resultSet.getInt("ratingScore"));
            productReview.setContent(resultSet.getString("content"));
            productReview.setIsShow(resultSet.getInt("isShow"));
            productReview.setCreatedAt(resultSet.getTimestamp("createdAt"));
            productReview.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
            return productReview;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
