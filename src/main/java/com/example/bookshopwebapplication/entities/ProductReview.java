package com.example.bookshopwebapplication.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Data
@ToString
@NoArgsConstructor
public class ProductReview {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer ratingScore;
    private String content;
    private Integer isShow;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private User user;
    private Product product;

    public ProductReview(Long id,
                         Long userId,
                         Long productId,
                         Integer ratingScore,
                         String content,
                         Integer isShow,
                         Timestamp createdAt,
                         Timestamp updatedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.ratingScore = ratingScore;
        this.content = content;
        this.isShow = isShow;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    // Format pattern cho SimpleDateFormat
    private static final String DATE_FORMAT_PATTERN = "HH:mm:ss dd/MM/yyyy";
    // Phương thức trả về createdAt đã được định dạng
    public String getFormattedCreatedAt() {
        return formatTimestamp(createdAt);
    }

    // Phương thức trả về updatedAt đã được định dạng
    public String getFormattedUpdatedAt() {
        return formatTimestamp(updatedAt);
    }

    // Phương thức chuyển đổi Timestamp thành chuỗi đã được định dạng
    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
            return dateFormat.format(timestamp);
        }
        return null;
    }
}
