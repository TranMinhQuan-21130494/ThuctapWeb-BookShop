package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.OrderDao;
import com.example.bookshopwebapplication.dto.OrderDto;
import com.example.bookshopwebapplication.dto.OrderItemDto;
import com.example.bookshopwebapplication.entities.Order;
import com.example.bookshopwebapplication.service._interface.IOrderService;
import com.example.bookshopwebapplication.service.transferObject.TOrder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderService implements IOrderService {

    private final OrderDao orderDao = new OrderDao();

    private UserService userService = new UserService();

    private TOrder tOrder = new TOrder();

    private static final OrderService instance = new OrderService();

    public static OrderService getInstance() {
        return instance;
    }

    // Phương thức để chèn một đối tượng OrderDto mới
    @Override
    public Optional<OrderDto> insert(OrderDto orderDto) {
        Long id = orderDao.save(tOrder.toEntity(orderDto));
        return getById(id);
    }

    // Phương thức để cập nhật thông tin của một đối tượng OrderDto
    @Override
    public Optional<OrderDto> update(OrderDto orderDto) {
        orderDao.update(tOrder.toEntity(orderDto));
        return getById(orderDto.getId());
    }

    // Phương thức để xóa các đối tượng Order theo danh sách các id
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) orderDao.delete(id);
    }

    // Phương thức để lấy đối tượng OrderDto dựa trên id
    @Override
    public Optional<OrderDto> getById(Long id) {
        Optional<Order> order = orderDao.getById(id);
        if (order.isPresent()) {
            OrderDto orderDto = tOrder.toDto(order.get());
            orderDto.setUser(UserService.getInstance().getById(order.get().getUserId()).get());
//            orderDto.setOrderItems(OrderItemService.getInstance().getByOrderId(order.get().getId()));
            orderDto.setUser(userService.getById(order.get().getUserId()).get());
            return Optional.of(orderDto);
        }
        return Optional.empty();
    }

    // Phương thức để lấy một phần của danh sách đối tượng OrderDto
    @Override
    public List<OrderDto> getPart(Integer limit, Integer offset) {
        return orderDao.getPart(limit, offset)
                .stream()
                .map(order -> getById(order.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // Phương thức để lấy một phần của danh sách đối tượng OrderDto và sắp xếp theo thứ tự
    @Override
    public List<OrderDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return orderDao.getOrderedPart(limit, offset, orderBy, sort).stream()
                .map(order -> getById(order.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // Phương thức để lấy một phần của danh sách đối tượng OrderDto và sắp xếp theo thứ tự, dựa trên userId
    @Override
    public List<OrderDto> getOrderedPartByUserId(long userId, int limit, int offset) {
        return orderDao.getOrderedPartByUserId(userId, limit, offset).stream()
                .map(order -> getById(order.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // Phương thức để đếm số lượng đơn hàng của một người dùng
    @Override
    public int countByUserId(long userId) {
        return orderDao.countByUserId(userId);
    }

    // Phương thức để hủy một đơn hàng
    @Override
    public double totalPrice(OrderDto orderDto) {
        double total = 0;
        for (OrderItemDto orderItemDto: orderDto.getOrderItems()){
            if (orderItemDto.getDiscount() == 0) {
                total += orderItemDto.getPrice() * orderItemDto.getQuantity();
            } else {
                total += (orderItemDto.getPrice() * (100 - orderItemDto.getDiscount()) / 100) * orderItemDto.getQuantity();
            }
        }
        return total;
    }

    @Override
    public void cancelOrder(long id) {
        orderDao.cancelOrder(id);
    }

    // Phương thức để đếm tổng số lượng đơn hàng
    @Override
    public int count() {
        return orderDao.count();
    }

    // Phương thức để xác nhận một đơn hàng
    @Override
    public void confirm(long id) {
        orderDao.confirm(id);
    }

    // Phương thức để hủy một đơn hàng
    @Override
    public void cancel(long id) {
        orderDao.cancel(id);
    }

    // Phương thức để reset trạng thái của một đơn hàng
    @Override
    public void reset(long id) {
        orderDao.reset(id);
    }
}
