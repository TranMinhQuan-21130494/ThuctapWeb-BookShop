package com.example.bookshopwebapplication.utils;

public class Paging {
    public static int offset(int page, int total, int show){
        // Tính tổng số trang (= tổng số total / số sản phẩm trên mỗi trang)
        int totalPages = totalPages(total, show);
        if (page < 1 || page > totalPages){
            page = 1;
        }
        // Tính mốc truy vấn (offset)
        int offset = (page - 1) * show;
        return offset;
    }
    public static int totalPages(int total, int show){
        int totalPages = total / show;
        if (total % show != 0){
            totalPages++;
        }
        return totalPages;
    }
}
