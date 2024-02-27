package com.example.bookshopwebapplication.dao.mapper;

import com.example.bookshopwebapplication.dao.mapper._interface.IRowMapper;
import com.example.bookshopwebapplication.entities.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapper implements IRowMapper<Category> {
    @Override
    public Category mapRow(ResultSet resultSet) {
        try {
            Category category = new Category();
            category.setId(resultSet.getLong("id"));
            category.setName(resultSet.getString("name"));
            category.setDescription(resultSet.getString("description"));
            category.setImageName(resultSet.getString("imageName"));
            return category;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
