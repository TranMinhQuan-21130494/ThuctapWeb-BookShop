package com.example.bookshopwebapplication.service;

import com.example.bookshopwebapplication.dao.CartItemDao;
import com.example.bookshopwebapplication.dto.CartDto;
import com.example.bookshopwebapplication.dto.CartItemDto;
import com.example.bookshopwebapplication.entities.Cart;
import com.example.bookshopwebapplication.entities.CartItem;
import com.example.bookshopwebapplication.service._interface.ICartItemService;
import com.example.bookshopwebapplication.service.transferObject.TCartItem;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartItemService implements ICartItemService {
    // Đối tượng CartItemDao được sử dụng để thao tác với cơ sở dữ liệu
    private final CartItemDao cartItemDao = new CartItemDao();

    // Đối tượng TCartItem được sử dụng để chuyển đổi giữa đối tượng DTO và đối tượng thực thể
    private TCartItem tCartItem = new TCartItem();

    // Singleton pattern: Đảm bảo rằng chỉ có một đối tượng CartItemService trong ứng dụng
    private static final CartItemService instance = new CartItemService();

    public static CartItemService getInstance() {
        return instance;
    }

    // Phương thức để lấy danh sách các đối tượng CartItemDto dựa trên cartId
    @Override
    public List<CartItemDto> getByCartId(long cartId) {
        List<CartItem> cartItems = cartItemDao.getByCartId(cartId);
        return cartItemDao.getByCartId(cartId)
                .stream()
                .map(cartItem -> getById(cartItem.getId())) // Chuyển đổi từ CartItem sang CartItemDto
                .filter(Optional::isPresent) // Lọc ra các đối tượng CartItemDto không rỗng
                .map(Optional::get) // Lấy giá trị thực sự của Optional<CartItemDto>
                .collect(Collectors.toList()); // Chuyển đổi thành danh sách
    }

    // Phương thức để lấy đối tượng CartItemDto dựa trên cartId và productId
    @Override
    public Optional<CartItemDto> getByCartIdAndProductId(long cartId, long productId) {
        return getById(cartItemDao.getByCartIdAndProductId(cartId, productId).get().getId());
    }

    // Phương thức để tính tổng số lượng dựa trên userId
    @Override
    public int sumQuantityByUserId(long userId) {
        return cartItemDao.sumQuantityByUserId(userId);
    }

    // Phương thức để chèn một đối tượng CartItemDto mới
    @Override
    public Optional<CartItemDto> insert(CartItemDto cartItemDto) {
        Long id = cartItemDao.save(tCartItem.toEntity(cartItemDto));
        return getById(id);
    }

    // Phương thức để cập nhật thông tin của một đối tượng CartItemDto
    @Override
    public Optional<CartItemDto> update(CartItemDto cartItemDto) {
        cartItemDao.update(tCartItem.toEntity(cartItemDto));
        return getById(cartItemDto.getId());
    }

    // Phương thức để xóa các đối tượng CartItem theo danh sách các id
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) cartItemDao.delete(id);
    }

    // Phương thức để lấy đối tượng CartItemDto dựa trên id
    @Override
    public Optional<CartItemDto> getById(Long id) {
        Optional<CartItem> cartItem = cartItemDao.getById(id);
        if (cartItem.isPresent()) {
            CartItemDto cartItemDto = tCartItem.toDto(cartItem.get());
            cartItemDto.setProduct(ProductService.getInstance()
                    .getById(cartItem.get().getProductId()).get());
            return Optional.of(cartItemDto);
        }
        return Optional.empty();
    }

    // Phương thức để lấy một phần của danh sách đối tượng CartItemDto
    @Override
    public List<CartItemDto> getPart(Integer limit, Integer offset) {
        return cartItemDao.getPart(limit, offset)
                .stream()
                .map(cartItem -> getById(cartItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    // Phương thức để lấy một phần của danh sách đối tượng CartItemDto và sắp xếp theo thứ tự
    @Override
    public List<CartItemDto> getOrderedPart(Integer limit, Integer offset, String orderBy, String sort) {
        return cartItemDao.getOrderedPart(limit, offset, orderBy, sort).stream()
                .map(cartItem -> getById(cartItem.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        return cartItemDao.count();
    }
}
