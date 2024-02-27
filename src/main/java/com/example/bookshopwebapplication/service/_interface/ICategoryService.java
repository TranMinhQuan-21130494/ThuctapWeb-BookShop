package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.dto.CategoryDto;
import com.example.bookshopwebapplication.entities.Category;

import java.util.Optional;

public interface ICategoryService extends IService<CategoryDto> {
    Optional<CategoryDto> getByProductId(long id);
}
