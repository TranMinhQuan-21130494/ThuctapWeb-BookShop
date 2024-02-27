package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.OrderItemDao;
import com.example.bookshopwebapplication.dto.OrderItemDto;
import com.example.bookshopwebapplication.entities.OrderItem;
import com.example.bookshopwebapplication.service._interface.IOrderItemService;
import com.example.bookshopwebapplication.service.transferObject.TCartItem;
import com.example.bookshopwebapplication.service.transferObject.TOrderItem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderItemService implements IOrderItemService {

    private OrderItemDao orderItemDao = new OrderItemDao();

    private TOrderItem tOrderItem = new TOrderItem();
    private static OrderItemService instance = new OrderItemService();

    public static OrderItemService getInstance() {
        return instance;
    }


    // Phương thức để chèn danh sách các đối tượng OrderItemDto
    @Override
    public void bulkInsert(List<OrderItemDto> orderItemDtoList) {
        for (OrderItemDto orderItemDto : orderItemDtoList) {
            this.insert(orderItemDto);
        }
    }

    // Phương thức để lấy danh sách tên sản phẩm dựa trên orderId
    @Override
    public String getProductNamesByOrderId(long orderId) {
        List<String> names = orderItemDao.getProductNamesByOrderId(orderId);
        if (names.size() == 1){
            return names.get(0);
        }
        return names.get(0) + " và " + (names.size() - 1) + " sản phẩm khác";
    }

    // Phương thức để lấy danh sách các đối tượng OrderItemDto dựa trên orderId
    @Override
    public List<OrderItemDto> getByOrderId(long orderId) {
        return orderItemDao.getByOrderId(orderId)
                .stream()
                .map(orderItem -> getById(orderItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // Phương thức để chèn một đối tượng OrderItemDto mới
    @Override
    public Optional<OrderItemDto> insert(OrderItemDto orderItemDto) {
        Long id = orderItemDao.save(tOrderItem.toEntity(orderItemDto));
        return getById(id);
    }

    // Phương thức để cập nhật thông tin của một đối tượng OrderItemDto
    @Override
    public Optional<OrderItemDto> update(OrderItemDto orderItemDto) {
        orderItemDao.update(tOrderItem.toEntity(orderItemDto));
        return getById(orderItemDto.getId());
    }

    // Phương thức để xóa các đối tượng OrderItem theo danh sách các id
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) orderItemDao.delete(id);
    }

    // Phương thức để lấy đối tượng OrderItemDto dựa trên id
    @Override
    public Optional<OrderItemDto> getById(Long id) {
        Optional<OrderItem> orderItem = orderItemDao.getById(id);
        if (orderItem.isPresent()) {
            OrderItemDto orderItemDto = tOrderItem.toDto(orderItem.get());
            orderItemDto.setOrder(OrderService.getInstance()
                    .getById(orderItem.get().getOrderId()).get());
            orderItemDto.setProduct(ProductService.getInstance()
                    .getById(orderItem.get().getProductId()).get());

            return Optional.of(orderItemDto);
        }
        return Optional.empty();
    }

    // Phương thức để lấy một phần của danh sách đối tượng OrderItemDto
    @Override
    public List<OrderItemDto> getPart(Integer limit, Integer offset) {

        return orderItemDao.getPart(limit, offset)
                .stream()
                .map(orderItem -> getById(orderItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // Phương thức để lấy một phần của danh sách đối tượng OrderItemDto và sắp xếp theo thứ tự
    @Override
    public List<OrderItemDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return orderItemDao.getOrderedPart(limit, offset, orderBy, sort)
                .stream()
                .map(orderItem -> getById(orderItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        return orderItemDao.count();
    }
}
