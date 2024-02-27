package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.dto.OrderItemDto;
import com.example.bookshopwebapplication.entities.OrderItem;

import java.util.List;

public interface IOrderItemService extends IService<OrderItemDto> {
    public void bulkInsert(List<OrderItemDto> orderItemDtoList);

    public String getProductNamesByOrderId(long orderId);

    public List<OrderItemDto> getByOrderId(long orderId);
}
