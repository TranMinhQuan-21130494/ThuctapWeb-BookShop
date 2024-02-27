package com.example.bookshopwebapplication.dao.mapper;

import com.example.bookshopwebapplication.dao.mapper._interface.IRowMapper;
import com.example.bookshopwebapplication.entities.Cart;
import com.example.bookshopwebapplication.entities.WishListItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartMapper implements IRowMapper<Cart> {
    @Override
    public Cart mapRow(ResultSet resultSet) {
        try {
            Cart cart = new Cart();
            cart.setId(resultSet.getLong("id"));
            cart.setUserId(resultSet.getLong("userId"));
            cart.setCreatedAt(resultSet.getTimestamp("createdAt"));
            if (resultSet.getTimestamp("updatedAt") != null)
                cart.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
            return cart;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
