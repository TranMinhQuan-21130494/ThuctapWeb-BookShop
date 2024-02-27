//
//package com.example.bookshopwebapplication.filter;
//
//import com.example.bookshopwebapplication.utils.ErrorMessage;
//import com.example.bookshopwebapplication.utils.JsonUtils;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
///**
// * Đây là một Filter trong Java Servlet API, được đặt tên là ExceptionFilter.
// * Nhiệm vụ của nó là bắt các ngoại lệ (RuntimeException) xảy ra trong quá trình xử lý request
// * và trả về một thông điệp lỗi dưới dạng JSON.*/
//@WebFilter(filterName = "ExceptionFilter", value = "/*")
//public class ExceptionFilter implements Filter {
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
//        try {
//            chain.doFilter(request, response);
//        } catch (RuntimeException e) {
//            ErrorMessage errorMessage = new ErrorMessage(400, e.toString());
//            JsonUtils.out(response, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
//        }
//    }
//}
