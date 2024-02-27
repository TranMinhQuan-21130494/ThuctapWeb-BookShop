package com.example.bookshopwebapplication.dao._interface;

import com.example.bookshopwebapplication.entities.Order;

import java.util.List;

public interface IOrderDao extends IGenericDao<Order>{
    public List<Order> getOrderedPartByUserId(long userId, int limit, int offset);
    public int countByUserId(long userId);
    public void cancelOrder(long id);
    public int count();
    public void confirm(long id);
    public void cancel(long id);
    public void reset(long id);
}
