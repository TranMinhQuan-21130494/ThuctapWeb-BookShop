package com.example.bookshopwebapplication.dao.mapper;

import com.example.bookshopwebapplication.dao.mapper._interface.IRowMapper;
import com.example.bookshopwebapplication.entities.User;
import com.example.bookshopwebapplication.entities.WishListItem;

import javax.swing.tree.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WishlistItemMapper implements IRowMapper<WishListItem> {
    @Override
    public WishListItem mapRow(ResultSet resultSet) {

        try {
            WishListItem wishListItem = new WishListItem();
            wishListItem.setId(resultSet.getLong("id"));
            wishListItem.setUserId(resultSet.getLong("userId"));
            wishListItem.setProductId(resultSet.getLong("productId"));
            wishListItem.setCreatedAt(resultSet.getTimestamp("createdAt"));
            return wishListItem;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
