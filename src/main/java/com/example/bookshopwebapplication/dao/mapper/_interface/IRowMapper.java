package com.example.bookshopwebapplication.dao.mapper._interface;

import java.sql.ResultSet;

public interface IRowMapper<T> {
    T mapRow(ResultSet resultSet);
}
