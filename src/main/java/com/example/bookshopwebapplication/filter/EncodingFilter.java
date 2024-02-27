package com.example.bookshopwebapplication.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
/**
* Đây là một Filter trong Java Servlet API được sử dụng để xử lý việc mã hóa ký tự đầu vào của request.
*/
@WebFilter(filterName = "EncodingFilter", value = "/*")
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
}
