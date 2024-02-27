package com.example.bookshopwebapplication.service._interface;

import com.example.bookshopwebapplication.dto.OrderDto;
import com.example.bookshopwebapplication.entities.Order;

import java.util.List;

public interface IOrderService extends IService<OrderDto> {
    public List<OrderDto> getOrderedPartByUserId(long userId, int limit, int offset);
    public int countByUserId(long userId);
    public double totalPrice(OrderDto orderDto);
    public void cancelOrder(long id);
    public int count();
    public void confirm(long id);
    public void cancel(long id);
    public void reset(long id);
}