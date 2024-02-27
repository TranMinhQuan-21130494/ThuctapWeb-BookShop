package com.example.bookshopwebapplication.dao.mapper;

import com.example.bookshopwebapplication.dao.mapper._interface.IRowMapper;
import com.example.bookshopwebapplication.entities.CartItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartItemMapper implements IRowMapper<CartItem> {
    @Override
    public CartItem mapRow(ResultSet resultSet) {
        try {
            CartItem cartItem = new CartItem();
            cartItem.setId(resultSet.getLong("id"));
            cartItem.setCartId(resultSet.getLong("cartId"));
            cartItem.setProductId(resultSet.getLong("productId"));
            cartItem.setQuantity(resultSet.getInt("quantity"));
            cartItem.setCreatedAt(resultSet.getTimestamp("createdAt"));
            if (resultSet.getTimestamp("createdAt") != null){
                cartItem.setUpdatedAt(resultSet.getTimestamp("updatedAt"));
            }
            return cartItem;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
