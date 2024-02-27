package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.entities.Product;

import java.util.List;
import java.util.Optional;

public interface IService<T> {
    Optional<T> insert(T t);

    Optional<T> update(T t);

    void delete(Long[] ids);
    Optional<T> getById(Long id);
    List<T> getPart(Integer limit, Integer offset);
    List<T> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort);
    int count();
}
