package com.example.e_shop.services;

import com.example.e_shop.dto.product.ProductRequest;

public interface ProductService {
    void add(ProductRequest productRequest, String token);
}
