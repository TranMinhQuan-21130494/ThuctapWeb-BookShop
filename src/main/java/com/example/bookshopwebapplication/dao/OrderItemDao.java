package com.example.bookshopwebapplication.dao;

import com.example.bookshopwebapplication.dao._interface.IOrderItemDao;
import com.example.bookshopwebapplication.dao.mapper.OrderItemMapper;
import com.example.bookshopwebapplication.entities.OrderItem;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class OrderItemDao extends AbstractDao<OrderItem> implements IOrderItemDao {

    // Phương thức để lưu một đối tượng OrderItem vào cơ sở dữ liệu
    public Long save(OrderItem orderItem) {
        clearSQL();
        builderSQL.append(
                "INSERT INTO " +
                        "order_item (orderId, productId, price, discount, quantity, " +
                        "createdAt) " +
                        "VALUES (?, ?, ?, ?, ?, ?)"
        );
        return insert(builderSQL.toString(), orderItem.getOrderId(), orderItem.getProductId(),
                orderItem.getPrice(), orderItem.getDiscount(), orderItem.getQuantity(),
                orderItem.getCreatedAt());
    }

    // Phương thức để cập nhật thông tin một đối tượng OrderItem trong cơ sở dữ liệu
    public void update(OrderItem orderItem) {
        clearSQL();
        builderSQL.append(
                "UPDATE order_item " +
                        "SET orderId = ?, productId = ?, " +
                        "price = ?, discount = ?, " +
                        "quantity = ?, createdAt = ?, " +
                        "updatedAt = ? " +
                        "WHERE id = ?"
        );
        update(builderSQL.toString(), orderItem.getOrderId(), orderItem.getProductId(),
                orderItem.getPrice(), orderItem.getDiscount(), orderItem.getQuantity(),
                orderItem.getCreatedAt(), orderItem.getCreatedAt(), orderItem.getId());
    }

    // Phương thức để xóa một đối tượng OrderItem khỏi cơ sở dữ liệu theo ID
    public void delete(Long id) {
        clearSQL();
        builderSQL.append("DELETE FROM order_item WHERE id = ?");
        update(builderSQL.toString(), id);
    }

    // Phương thức để lấy một đối tượng OrderItem từ cơ sở dữ liệu theo ID
    public Optional<OrderItem> getById(Long id) {
        clearSQL();
        builderSQL.append("SELECT * FROM order_item WHERE id = ?");
        return Optional.ofNullable(query(builderSQL.toString(), new OrderItemMapper(), id).get(0));
    }

    // Phương thức để lấy một phần danh sách OrderItem từ cơ sở dữ liệu với giới hạn và vị trí bắt đầu
    public List<OrderItem> getPart(Integer limit, Integer offset) {
        clearSQL();
        builderSQL.append("SELECT * FROM order_item LIMIT " + offset + ", " + limit);
        List<OrderItem> orderItems = query(builderSQL.toString(), new OrderItemMapper());
        return orderItems.isEmpty() ? new LinkedList<>() : orderItems;
    }

    // Phương thức để lấy một phần danh sách OrderItem từ cơ sở dữ liệu với sắp xếp theo các thuộc tính được chỉ định
    public List<OrderItem> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        clearSQL();
        builderSQL.append("SELECT * FROM order_item ORDER BY " + orderBy + " " + sort);
        builderSQL.append(" LIMIT " + offset + ", " + limit + " ");
        List<OrderItem> orderItems = query(builderSQL.toString(), new OrderItemMapper());
        return orderItems.isEmpty() ? new LinkedList<>() : orderItems;
    }
    public int count (){
        clearSQL();
        builderSQL.append(
                "SELECT COUNT(*) FROM order_item"
        );
        return count(builderSQL.toString());
    }

    // Phương thức để thêm một danh sách OrderItem vào cơ sở dữ liệu
    @Override
    public void bulkInsert(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) this.save(orderItem);
    }

    // Phương thức để lấy danh sách tên sản phẩm từ cơ sở dữ liệu theo ID đơn hàng
    @Override
    public List<String> getProductNamesByOrderId(long orderId) {
        try {
            clearSQL();
            builderSQL.append(
                    "SELECT name " +
                            "FROM product p " +
                            "JOIN order_item o ON p.id = o.productId " +
                            "WHERE o.orderId = ?"
            );
            List<String> result = new LinkedList<>();
            connection = getConnection();
            statement = connection.prepareStatement(builderSQL.toString());
            statement.setLong(1, orderId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("name"));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // Đảm bảo đóng các tài nguyên liên quan đến cơ sở dữ liệu
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                return null;
            }
        }
    }

    // Phương thức để lấy danh sách OrderItem từ cơ sở dữ liệu theo ID đơn hàng
    @Override
    public List<OrderItem> getByOrderId(long orderId) {
        clearSQL();
        builderSQL.append(
                "SELECT * FROM order_item WHERE orderId = ?"
        );
        List<OrderItem> orderItems = query(builderSQL.toString(), new OrderItemMapper(), orderId);
        return orderItems.isEmpty() ? new LinkedList<>() : orderItems;
    }
}
