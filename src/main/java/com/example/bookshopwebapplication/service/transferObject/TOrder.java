package com.example.bookshopwebapplication.service.transferObject;

import com.example.bookshopwebapplication.dto.OrderDto;
import com.example.bookshopwebapplication.entities.Order;

public class TOrder implements ITransfer<OrderDto, Order> {
    @Override
    public OrderDto toDto(Order entity) {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(entity.getId());
        orderDto.setStatus(entity.getStatus());
        orderDto.setDeliveryMethod(entity.getDeliveryMethod());
        orderDto.setDeliveryPrice(entity.getDeliveryPrice());
        orderDto.setCreatedAt(entity.getCreatedAt());
        orderDto.setUpdatedAt(entity.getUpdatedAt());
        return orderDto;
    }

    @Override
    public Order toEntity(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setUserId(dto.getUser().getId());
        order.setStatus(dto.getStatus());
        order.setDeliveryMethod(dto.getDeliveryMethod());
        order.setDeliveryPrice(dto.getDeliveryPrice());
        order.setCreatedAt(dto.getCreatedAt());
        order.setUpdatedAt(dto.getUpdatedAt());
        return order;
    }
}
