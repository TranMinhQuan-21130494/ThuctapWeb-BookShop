package com.example.bookshopwebapplication.servlet.client;

import com.example.bookshopwebapplication.dto.CartDto;
import com.example.bookshopwebapplication.dto.CartItemDto;
import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.message.Message;
import com.example.bookshopwebapplication.network.CartItemRequest;
import com.example.bookshopwebapplication.network.CartItemResponse;
import com.example.bookshopwebapplication.network.CartResponse;
import com.example.bookshopwebapplication.service.CartItemService;
import com.example.bookshopwebapplication.service.CartService;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.JsonUtils;
import com.example.bookshopwebapplication.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/cartItem")
public class CartItemServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    private final CartItemService cartItemService = new CartItemService();
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();
    private Long cartId;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy userId và đối tượng user từ database theo userId này
        long userId = Protector.of(() -> Long.parseLong(request.getParameter("userId"))).get(0L);
        Optional<UserDto> userFromServer = Protector.of(() -> userService.getById(userId)).get(Optional::empty);

        // Nếu userId là số nguyên dương và có hiện diện trong bảng user
        if (userId > 0L && userFromServer.isPresent()) {
            // Lấy đối tượng cart từ database theo userId
            Optional<CartDto> cartFromServer = cartService.getByUserId(userId);

            // Nếu cart của user này đã có trong database
            if (cartFromServer.isPresent()) {
                cartId = cartFromServer.get().getId();
                List<CartItemDto> cartItems = Protector.of(() ->
                        cartItemService.getByCartId(cartId)).get(ArrayList::new);

                // set Cart for CartItem
                cartItems.forEach(cartItemDto -> cartItemDto.setCart(CartService.getInstance().getById(cartId).get()));

                List<CartItemResponse> cartItemResponses = cartItems.stream().map(cartItem -> new CartItemResponse(
                        cartItem.getId(),
                        cartItem.getCart().getId(),
                        cartItem.getProduct().getId(),
                        cartItem.getProduct().getName(),
                        cartItem.getProduct().getPrice(),
                        cartItem.getProduct().getDiscount(),
                        cartItem.getProduct().getQuantity(),
                        cartItem.getProduct().getImageName(),
                        cartItem.getQuantity()
                )).collect(Collectors.toList());

                CartResponse cartResponse = new CartResponse(cartId, userId, cartItemResponses);
                JsonUtils.out(response, cartResponse, HttpServletResponse.SC_OK);
            } else {
                CartResponse cartResponse = new CartResponse(0L, userId, Collections.emptyList());
                JsonUtils.out(response, cartResponse, HttpServletResponse.SC_OK);
            }
        } else {
            String errorMessage = "Đã có lỗi truy vấn!";
            JsonUtils.out(response, new Message(404, errorMessage), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    //
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy đối tượng cartItemRequest từ JSON trong request
        CartItemRequest cartItemRequest = JsonUtils.get(request, CartItemRequest.class);

        // Lấy đối tượng cart từ database theo userId từ cartItemRequest
        Optional<CartDto> cartFromServer = Protector.of(() -> cartService.getByUserId(cartItemRequest.getUserId()))
                .get(Optional::empty);

        // Nhận cartId từ cartFromServer (nếu đã có) hoặc cart mới (nếu chưa có)
        long cartId;

        if (cartFromServer.isPresent()) {
            cartId = cartFromServer.get().getId();
        } else {
            CartDto cart = new CartDto();
            cart.setUser((UserDto) request.getSession().getAttribute("currentUser"));
            cart.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            cart.setUpdatedAt(null);

            cartId = Protector.of(() -> cartService.insert(cart).get().getId()).get(0L);
        }

        String successMessage = "Đã thêm sản phẩm vào giỏ hàng thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new Message(200, successMessage),
                HttpServletResponse.SC_OK);
        Runnable failFunction = () -> JsonUtils.out(
                response,
                new Message(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        // Nếu cart của user này đã có trong database (cardId lớn hơn O)
        if (cartId > 0L) {
            // Lấy đối tượng cartItem từ database theo cartId và productId của cartItemRequest
            Optional<CartItemDto> cartItemFromServer = Protector.of(() -> cartItemService.getByCartIdAndProductId(
                    cartId, cartItemRequest.getProductId()
            )).get(Optional::empty);

            // Nếu cartItem của cartId và productId này đã có trong database
            if (cartItemFromServer.isPresent()) {
                CartItemDto cartItem = cartItemFromServer.get();
                cartItem.setCart(cartService.getById(cartId).get());
                cartItem.setProduct(productService.getById(cartItemRequest.getProductId()).get());
                cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
                cartItem.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

                Protector.of(() -> cartItemService.update(cartItem))
                        .done(r -> doneFunction.run())
                        .fail(e -> failFunction.run());
            } else {
                CartItemDto cartItem = new CartItemDto();
                cartItem.setCart(cartService.getById(cartId).get());
                cartItem.setProduct(productService.getById(cartItemRequest.getProductId()).get());
                cartItem.setQuantity(cartItemRequest.getQuantity());
                cartItem.setCreatedAt(new Timestamp(System.currentTimeMillis()));
                System.out.println(cartItem);
                Protector.of(() -> cartItemService.insert(cartItem))
                        .done(r -> doneFunction.run())
                        .fail(e -> failFunction.run());
            }
        } else {
            failFunction.run();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CartItemRequest cartItemRequest = JsonUtils.get(request, CartItemRequest.class);

        long cartItemId = Protector.of(() -> Long.parseLong(request.getParameter("cartItemId"))).get(0L);

        Optional<CartItemDto> cartItemDto = Protector.of(() -> cartItemService.getById(cartItemId)).get(Optional::empty);
        cartItemDto.get().setCart(CartService.getInstance().getById(cartId).get());

        String successMessage = "Đã cập nhật số lượng của sản phẩm thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new Message(200, successMessage),
                HttpServletResponse.SC_OK);
        Runnable failFunction = () -> JsonUtils.out(
                response,
                new Message(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        if (cartItemId > 0L && cartItemDto.isPresent()) {
            cartItemDto.get().setQuantity(cartItemRequest.getQuantity());
            Protector.of(() -> cartItemService.update(cartItemDto.get()))
                    .done(r -> doneFunction.run())
                    .fail(e -> failFunction.run());
        } else {
            failFunction.run();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long cartItemId = Protector.of(() -> Long.parseLong(request.getParameter("cartItemId"))).get(0L);

        String successMessage = "Đã xóa sản phẩm khỏi giỏ hàng thành công!";
        String errorMessage = "Đã có lỗi truy vấn!";

        Runnable doneFunction = () -> JsonUtils.out(
                response,
                new Message(200, successMessage),
                HttpServletResponse.SC_OK);
        Runnable failFunction = () -> JsonUtils.out(
                response,
                new Message(404, errorMessage),
                HttpServletResponse.SC_NOT_FOUND);

        if (cartItemId > 0L) {
            Protector.of(() -> cartItemService.delete(new Long[]{cartItemId}))
                    .done(r -> doneFunction.run())
                    .fail(e -> failFunction.run());
        } else {
            failFunction.run();
        }
    }
}
