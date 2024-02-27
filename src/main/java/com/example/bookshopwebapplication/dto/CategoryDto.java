package com.example.bookshopwebapplication.dto;

import com.example.bookshopwebapplication.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@ToString
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private String imageName;
    private List<ProductDto> products;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageName() {
        return imageName;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public CategoryDto(Long id,
                       String name,
                       String description,
                       String imageName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageName = imageName;
        products = new ArrayList<>();
    }
}
