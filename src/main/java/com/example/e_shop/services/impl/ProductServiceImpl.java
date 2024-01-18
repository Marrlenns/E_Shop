package com.example.e_shop.services.impl;

import com.example.e_shop.dto.product.ProductRequest;
import com.example.e_shop.entities.Product;
import com.example.e_shop.enums.Role;
import com.example.e_shop.enums.Type;
import com.example.e_shop.exceptions.BadCredentialsException;
import com.example.e_shop.exceptions.BadRequestException;
import com.example.e_shop.repositories.ProductRepository;
import com.example.e_shop.services.AuthService;
import com.example.e_shop.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AuthService authService;

    public boolean findByName(String name) {
        for (Type direction : Type.values())
            if (direction.name().equalsIgnoreCase(name))
                return true;
        return false;
    }

    @Override
    public void add(ProductRequest productRequest, String token) {

        if(!authService.getUserFromToken(token).getRole().equals(Role.ADMIN))
            throw new BadCredentialsException("This function is available only for admin!");

        Optional<Product> isProduct = productRepository.findByCode(productRequest.getCode());
        if(isProduct.isPresent()){
            throw new BadRequestException("Product with this code is already exist!\nTry other code!");
        }
        if(!findByName(productRequest.getType()))
            throw new BadRequestException("This type of value doesn't exist!");

        Product product = new Product();
        product.setTitle(productRequest.getTitle());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription());
        product.setType(Type.valueOf(productRequest.getType()));
        product.setCode(productRequest.getCode());
        product.setAgeAccess(productRequest.getAgeAccess());

        productRepository.save(product);
    }
}
