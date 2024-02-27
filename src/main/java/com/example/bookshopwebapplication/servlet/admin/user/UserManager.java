package com.example.bookshopwebapplication.servlet.admin.user;

import com.example.bookshopwebapplication.dto.UserDto;
import com.example.bookshopwebapplication.service.UserService;
import com.example.bookshopwebapplication.utils.Paging;
import com.example.bookshopwebapplication.utils.Protector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(value = "/admin/userManager")
public class UserManager extends HttpServlet {
    private final static int USERS_PER_PAGE = 4;
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int totalUsers = Protector.of(userService::count).get(0);

        String pageParam = Optional.ofNullable(request.getParameter("page")).orElse("1");
        int page = Protector.of(() -> Integer.parseInt(pageParam)).get(1);

        int totalPages = Paging.totalPages(totalUsers, USERS_PER_PAGE);
        int offset = Paging.offset(page, totalUsers, USERS_PER_PAGE);

        List<UserDto> users = Protector.of(() -> userService.getOrderedPart(
                USERS_PER_PAGE, offset, "id", "DESC"
        )).get(ArrayList::new);

        Optional<String> action = Optional.ofNullable(request.getParameter("action"));
        if (action.isPresent() && action.get().equals("delete")) {
            Long id = Long.parseLong(request.getParameter("id"));
            String success = String.format("Xóa user %s thành công!", userService.getById(id).get().getUsername());
            String fail = String.format("Xóa user %s thất bại!", userService.getById(id).get().getUsername());
            Protector.of(() -> userService.delete(new Long[]{id}))
                    .done(s -> request.getSession().setAttribute("successMessage", success))
                    .fail(e -> request.getSession().setAttribute("errorMessage", fail));
            response.sendRedirect(request.getContextPath() + "/admin/userManager");
            return;
        }
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("page", page);
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/admin/user/userManager.jsp").forward(request, response);
    }
}
