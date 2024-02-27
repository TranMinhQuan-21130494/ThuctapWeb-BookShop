package com.example.bookshopwebapplication.service.transferObject;

import com.example.bookshopwebapplication.dto.ProductDto;
import com.example.bookshopwebapplication.entities.Product;

public class TProduct implements ITransfer<ProductDto, Product> {
    @Override
    public ProductDto toDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDiscount(product.getDiscount());
        productDto.setQuantity(product.getQuantity());
        productDto.setTotalBuy(product.getTotalBuy());
        productDto.setAuthor(product.getAuthor());
        productDto.setPages(product.getPages());
        productDto.setPublisher(product.getPublisher());
        productDto.setYearPublishing(product.getYearPublishing());
        productDto.setDescription(product.getDescription());
        productDto.setImageName(product.getImageName());
        productDto.setShop(product.getShop());
        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setUpdatedAt(product.getUpdatedAt());
        return productDto;
    }

    @Override
    public Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setQuantity(productDto.getQuantity());
        product.setTotalBuy(productDto.getTotalBuy());
        product.setAuthor(productDto.getAuthor());
        product.setPages(productDto.getPages());
        product.setPublisher(productDto.getPublisher());
        product.setYearPublishing(productDto.getYearPublishing());
        product.setDescription(productDto.getDescription());
        product.setImageName(productDto.getImageName());
        product.setShop(productDto.getShop());
        product.setCreatedAt(productDto.getCreatedAt());
        product.setUpdatedAt(productDto.getUpdatedAt());
        return product;
    }
}
