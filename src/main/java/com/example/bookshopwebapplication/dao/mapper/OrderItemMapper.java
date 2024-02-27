package com.example.bookshopwebapplication.dao.mapper;

import com.example.bookshopwebapplication.dao.mapper._interface.IRowMapper;
import com.example.bookshopwebapplication.entities.Cart;
import com.example.bookshopwebapplication.entities.OrderItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemMapper implements IRowMapper<OrderItem> {
    @Override
    public OrderItem mapRow(ResultSet resultSet) {
        try {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(resultSet.getLong("id"));
            orderItem.setOrderId(resultSet.getLong("orderId"));
            orderItem.setProductId(resultSet.getLong("productId"));
            orderItem.setPrice(resultSet.getDouble("price"));
            orderItem.setDiscount(resultSet.getDouble("discount"));
            orderItem.setQuantity(resultSet.getInt("quantity"));
            orderItem.setCreatedAt(resultSet.getTimestamp("createdAt"));
            orderItem.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
            return orderItem;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
