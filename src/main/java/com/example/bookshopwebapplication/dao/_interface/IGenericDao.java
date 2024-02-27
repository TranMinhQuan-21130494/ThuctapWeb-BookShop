package com.example.bookshopwebapplication.dao._interface;

import com.example.bookshopwebapplication.dao.mapper._interface.IRowMapper;

import java.util.List;
import java.util.Optional;

public interface IGenericDao<T> {
    List<T> query(String sql, IRowMapper<T> rowMapper, Object... parameters);

    void update(String sql, Object... parameters);

    Long insert(String sql, Object... parameters);
    Optional<T> getById(String sql, IRowMapper<T> rowMapper, Object... parameters);

    List<T> getAll(String sql, IRowMapper<T> mapper, Object... parameters);

    List<T> getPart(String sql, IRowMapper<T> mapper, Object... parameters);

    //int limit, int offset, String orderBy, String orderDir
    List<T> getOrderedPart(String sql, IRowMapper<T> mapper, Object... parameters);
    int count(String sql, Object... parameters);
    Long getIdElement(String sql, Object... parameters);
}
