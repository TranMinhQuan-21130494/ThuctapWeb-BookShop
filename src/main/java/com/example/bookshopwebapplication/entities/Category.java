package com.example.bookshopwebapplication.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class Category {
    private Long id;
    private String name;
    private String description;
    private String imageName;
    private List<Product> products;
    public Category(Long id,
                    String name,
                    String description,
                    String imageName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageName = imageName;
    }
}
