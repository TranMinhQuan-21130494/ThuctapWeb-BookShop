package com.example.bookshopwebapplication.dao.mapper;

import com.example.bookshopwebapplication.dao.mapper._interface.IRowMapper;
import com.example.bookshopwebapplication.entities.Cart;
import com.example.bookshopwebapplication.entities.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements IRowMapper<Order> {

    @Override
    public Order mapRow(ResultSet resultSet) {
        try {
            Order order = new Order();
            order.setId(resultSet.getLong("id"));
            order.setUserId(resultSet.getLong("userId"));
            order.setStatus(resultSet.getInt("status"));
            order.setDeliveryMethod(resultSet.getInt("deliveryMethod"));
            order.setDeliveryPrice(resultSet.getDouble("deliveryPrice"));
            order.setCreatedAt(resultSet.getTimestamp("createdAt"));
            order.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
