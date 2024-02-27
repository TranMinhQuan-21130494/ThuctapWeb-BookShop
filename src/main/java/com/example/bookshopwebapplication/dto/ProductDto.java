package com.example.bookshopwebapplication.dto;

import com.example.bookshopwebapplication.entities.Category;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@ToString
public class ProductDto {
    private Long id;
    private String name;
    private Double price;
    private Double discount;
    private Integer quantity;
    private Integer totalBuy;
    private String author;
    private Integer pages;
    private String publisher;
    private Integer yearPublishing;
    private String description;
    private String imageName;
    private Integer shop;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ProductDto(Long id,
                      String name,
                      Double price,
                      Double discount,
                      Integer quantity,
                      Integer totalBuy,
                      String author,
                      Integer pages,
                      String publisher,
                      Integer yearPublishing,
                      String description,
                      String imageName,
                      Integer shop,
                      Timestamp createdAt,
                      Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.totalBuy = totalBuy;
        this.author = author;
        this.pages = pages;
        this.publisher = publisher;
        this.yearPublishing = yearPublishing;
        this.description = description;
        this.imageName = imageName;
        this.shop = shop;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
