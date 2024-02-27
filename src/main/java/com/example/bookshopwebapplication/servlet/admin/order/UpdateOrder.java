package com.example.bookshopwebapplication.servlet.admin.order;

import com.example.bookshopwebapplication.dto.OrderDto;
import com.example.bookshopwebapplication.dto.OrderItemDto;
import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.service.OrderItemService;
import com.example.bookshopwebapplication.service.OrderService;
import com.example.bookshopwebapplication.service.ProductService;
import com.example.bookshopwebapplication.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/orderManager/update")
public class UpdateOrder extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Protector.of(() -> Long.parseLong(request.getParameter("id"))).get(0L);
        String action = request.getParameter("action");


        String errorMessage = "Đã có lỗi truy vấn!";

        if ("CONFIRM".equals(action)) {
            String successMessage = String.format("Đã xác nhận đã giao đơn hàng #%s thành công!", id);
            Protector.of(() -> {
                        orderService.confirm(id);
                    })
                    .done(r -> request.getSession().setAttribute("successMessage", successMessage))
                    .fail(e -> request.getSession().setAttribute("errorMessage", errorMessage));
            Optional<OrderDto> orderDto = orderService.getById(id);
            List<OrderItemDto> itemDtoList = OrderItemService.getInstance().getByOrderId(orderDto.get().getId());
            for (OrderItemDto orderItemDto: itemDtoList){
                ProductDto productDto = orderItemDto.getProduct();
                productDto.setQuantity(productDto.getQuantity() - 1);
                productDto.setTotalBuy(productDto.getTotalBuy() + 1);

                Protector.of(() -> ProductService.getInstance().update(productDto));
            }

        }
        if ("CANCEL".equals(action)) {
            String successMessage = String.format("Đã hủy đơn hàng #%s thành công!", id);
            Protector.of(() -> orderService.cancel(id))
                    .done(r -> request.getSession().setAttribute("successMessage", successMessage))
                    .fail(e -> request.getSession().setAttribute("errorMessage", errorMessage));
        }
        if ("RESET".equals(action)) {
            String successMessage = String.format("Đã đặt lại trạng thái là đang giao hàng cho đơn hàng #%s thành công!", id);
            Protector.of(() -> orderService.reset(id))
                    .done(r -> request.getSession().setAttribute("successMessage", successMessage))
                    .fail(e -> request.getSession().setAttribute("errorMessage", errorMessage));
        }

        response.sendRedirect(request.getContextPath() + "/admin/orderManager");
    }
}
