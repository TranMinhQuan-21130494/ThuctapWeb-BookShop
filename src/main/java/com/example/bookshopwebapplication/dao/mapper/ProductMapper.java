package com.example.bookshopwebapplication.dao.mapper;

import com.example.bookshopwebapplication.dao.mapper._interface.IRowMapper;
import com.example.bookshopwebapplication.entities.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements IRowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet) {
        try {
            Product product = new Product();
            product.setId(resultSet.getLong("id"));
            product.setName(resultSet.getString("name"));
            product.setPrice(resultSet.getDouble("price"));
            product.setDiscount(resultSet.getDouble("discount"));
            product.setQuantity(resultSet.getInt("quantity"));
            product.setTotalBuy(resultSet.getInt("totalBuy"));
            product.setAuthor(resultSet.getString("author"));
            product.setPages(resultSet.getInt("pages"));
            product.setPublisher(resultSet.getString("publisher"));
            product.setYearPublishing(resultSet.getInt("yearPublishing"));
            product.setDescription(resultSet.getString("description"));
            product.setImageName(resultSet.getString("imageName"));
            product.setShop(resultSet.getInt("shop"));
            product.setCreatedAt(resultSet.getTimestamp("createdAt"));
            product.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
