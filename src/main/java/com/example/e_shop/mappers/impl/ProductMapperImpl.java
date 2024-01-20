package com.example.e_shop.mappers.impl;

import com.example.e_shop.dto.product.ProductDetailResponse;
import com.example.e_shop.dto.product.ProductResponse;
import com.example.e_shop.entities.Product;
import com.example.e_shop.mappers.ProductMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ProductMapperImpl implements ProductMapper {

    public ProductResponse toDto(Product product){
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setPrice(product.getPrice());
        return response;
    }

    @Override
    public List<ProductResponse> toDtos(List<Product> all) {
        List<ProductResponse> products = new ArrayList<>();
        for(Product product: all){
            products.add(toDto(product));
        }
        return products;
    }

    @Override
    public ProductDetailResponse toDetailDto(Product product) {
        ProductDetailResponse response = new ProductDetailResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setType(String.valueOf(product.getType()));
        response.setCode(product.getCode());
        response.setAgeAccess(product.getAgeAccess());
        if(product.getClient() == null)
            response.setClientName("There is no client");
        else
            response.setClientName(product.getClient().getFullName());
        return response;
    }
}