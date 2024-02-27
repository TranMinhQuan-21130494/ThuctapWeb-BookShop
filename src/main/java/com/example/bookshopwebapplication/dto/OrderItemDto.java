package com.example.bookshopwebapplication.dto;

import com.example.bookshopwebapplication.entities.Order;
import com.example.bookshopwebapplication.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
@Data
@ToString
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private Double price;
    private Double discount;
    private Integer quantity;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private ProductDto product;
    private OrderDto order;
    public OrderItemDto(Long id,
                     Double price,
                     Double discount,
                     Integer quantity,
                     Timestamp createdAt,
                     Timestamp updatedAt) {
        this.id = id;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public OrderItemDto(Double price,
                        Double discount,
                        Integer quantity,
                        Timestamp createdAt,
                        Timestamp updatedAt,
                        OrderDto orderDto,
                        ProductDto productDto) {
        this.id = id;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.order = orderDto;
        this.product = productDto;
    }
}
