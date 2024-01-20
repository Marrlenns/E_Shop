package com.example.e_shop.mappers;

import com.example.e_shop.dto.product.ProductDetailResponse;
import com.example.e_shop.dto.product.ProductResponse;
import com.example.e_shop.entities.Product;

import java.util.List;

public interface ProductMapper {
    List<ProductResponse> toDtos(List<Product> all);

    ProductDetailResponse toDetailDto(Product product);
}
