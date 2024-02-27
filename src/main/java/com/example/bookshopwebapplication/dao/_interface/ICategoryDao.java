package com.example.bookshopwebapplication.dao._interface;

import com.example.bookshopwebapplication.entities.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryDao extends IGenericDao<Category>{
    List<Category> getAll();

    Optional<Category> getByProductId(long id);
}
