package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IOrderDao;
import com.example.bookshopwebapplication.dao.mapper.OrderMapper;
import com.example.bookshopwebapplication.entities.Order;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OrderDao extends AbstractDao<Order> implements IOrderDao {

    // Lưu một đơn hàng mới vào cơ sở dữ liệu.
    public Long save(Order order) {
        clearSQL();
        builderSQL.append(
                "INSERT INTO " +
                        "orders (userId, status, deliveryMethod," +
                        " deliveryPrice, createdAt)" +
                        " VALUES (?, ?, ?, ?, ?)"
        );
        return insert(builderSQL.toString(), order.getUserId(), order.getStatus(),
                order.getDeliveryMethod(), order.getDeliveryPrice(), order.getCreatedAt());
    }

    //Cập nhật thông tin của một đơn hàng trong cơ sở dữ liệu.
    public void update(Order order) {
        clearSQL();
        builderSQL.append(
                "UPDATE orders " +
                        "SET userId = ?, status = ?," +
                        " deliveryMethod = ?, " +
                        "deliveryPrice = ?, createdAt = ?," +
                        " updatedAt = ?" +
                        " WHERE id = ?"
        );
        update(builderSQL.toString(), order.getUserId(), order.getStatus(),
                order.getDeliveryMethod(), order.getDeliveryPrice(),
                order.getCreatedAt(), new Timestamp(System.currentTimeMillis()),
                order.getId());
    }

    //Xóa một đơn hàng khỏi cơ sở dữ liệu dựa trên id.
    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM orders WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    //Lấy đơn hàng dựa trên id.
    public Optional<Order> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM orders WHERE id = ?");
        return Optional.ofNullable(query(builderSQL.toString(), new OrderMapper(), id).get(0));
    }

    // Lấy danh sách các đơn hàng với giới hạn số lượng và vị trí bắt đầu.
    public List<Order> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM orders LIMIT " + offset + ", " + limit);
        List<Order> orders = query(builderSQL.toString(), new OrderMapper());
        return orders.isEmpty() ? new LinkedList<>() : orders;
    }

    //Lấy danh sách các đơn hàng được sắp xếp theo một trường và thứ tự cụ thể.
    public List<Order> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM orders ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + " ");
        List<Order> orders = query(builderSQL.toString(), new OrderMapper());
        return orders.isEmpty() ? new LinkedList<>() : orders;
    }

     //Lấy danh sách các đơn hàng được sắp xếp theo người dùng và giới hạn số lượng và vị trí bắt đầu.
    @Override
    public List<Order> getOrderedPartByUserId(long userId, int limit, int offset) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM orders " +
                        "WHERE userId = ? " +
                        "ORDER BY orders.createdAt DESC " +
                        "LIMIT " + offset + ", " + limit
        );
        List<Order> orders = query(builderSQL.toString(), new OrderMapper(), userId);
        return orders.isEmpty() ? new LinkedList<>(): orders;
    }

    // Đếm số lượng đơn hàng dựa trên id người dùng.
    @Override
    public int countByUserId(long userId) {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(orders.id) FROM orders WHERE userId = ?"
        );
        return count(builderSQL.toString(), userId);
    }

    //Hủy đơn hàng dựa trên id.
    @Override
    public void cancelOrder(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE orders SET status = 3, updatedAt = NOW() WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }

    //Đếm tổng số lượng đơn hàng.
    @Override
    public int count() {
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(id) FROM orders"
        );
        return count(builderSQL.toString());
    }

    //Xác nhận một đơn hàng dựa trên id.
    @Override
    public void confirm(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE orders SET status = 2, updatedAt = NOW() WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }

    //Hủy một đơn hàng dựa trên id.
    @Override
    public void cancel(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE orders SET status = 3, updatedAt = NOW() WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }

    //Thiết lập lại trạng thái của một đơn hàng dựa trên id.
    @Override
    public void reset(long id) {
        clearSQL();
        builderSQL.append(
                "UPDATE orders SET status = 1, updatedAt = NOW() WHERE id = ?"
        );
        update(builderSQL.toString(), id);
    }
}