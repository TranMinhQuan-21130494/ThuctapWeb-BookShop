package com.example.bookshopwebapplication.service.transferObject;

import com.example.bookshopwebapplication.dto.OrderItemDto;
import com.example.bookshopwebapplication.entities.OrderItem;

public class TOrderItem implements ITransfer<OrderItemDto, OrderItem> {
    @Override
    public OrderItemDto toDto(OrderItem entity) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(entity.getId());
        orderItemDto.setPrice(entity.getPrice());
        orderItemDto.setDiscount(entity.getDiscount());
        orderItemDto.setQuantity(entity.getQuantity());
        orderItemDto.setCreatedAt(entity.getCreatedAt());
        orderItemDto.setUpdatedAt(entity.getUpdatedAt());
        return orderItemDto;
    }

    @Override
    public OrderItem toEntity(OrderItemDto dto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(dto.getId());
        orderItem.setOrderId(dto.getOrder().getId());
        orderItem.setProductId(dto.getProduct().getId());
        orderItem.setPrice(dto.getPrice());
        orderItem.setDiscount(dto.getDiscount());
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setCreatedAt(dto.getCreatedAt());
        orderItem.setUpdatedAt(dto.getUpdatedAt());
        return orderItem;
    }
}
