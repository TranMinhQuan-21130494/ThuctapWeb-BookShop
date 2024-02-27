package com.example.bookshopwebapplication.dao._interface;

import com.example.bookshopwebapplication.entities.OrderItem;

import java.util.List;

public interface IOrderItemDao extends IGenericDao<OrderItem>{
    public void bulkInsert(List<OrderItem> orderItems);

    public List<String> getProductNamesByOrderId(long orderId);

    public List<OrderItem> getByOrderId(long orderId);
}
