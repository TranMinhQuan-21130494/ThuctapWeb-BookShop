package com.example.bookshopwebapplication.service.transferObject;

import com.example.bookshopwebapplication.dto.CategoryDto;
import com.example.bookshopwebapplication.entities.Category;

public class TCategory implements ITransfer<CategoryDto, Category> {
    @Override
    public CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        categoryDto.setImageName(category.getImageName());
        return categoryDto;
    }

    @Override
    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setImageName(categoryDto.getImageName());
        return category;
    }
}
