package com.example.e_shop.services;

import com.example.e_shop.dto.product.ProductDetailResponse;
import com.example.e_shop.dto.product.ProductRequest;
import com.example.e_shop.dto.product.ProductResponse;

import java.util.List;

public interface ProductService {
    void add(ProductRequest productRequest, String token);

    List<ProductResponse> all(String token);

    void delete(String token, String code);

    ProductDetailResponse findById(Long id);

    ProductDetailResponse updateByCode(String token, String code, ProductRequest request);
}
