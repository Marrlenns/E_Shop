package com.example.e_shop.controllers;

import com.example.e_shop.dto.product.ProductRequest;
import com.example.e_shop.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public String hello(){
        return "marlen salam";
    }

    @PostMapping("/add")
    public String add(@RequestBody ProductRequest productRequest, @RequestHeader("Authorization") String token){
        productService.add(productRequest, token);
        return "Product: " + productRequest.getTitle() + " - added successfully!";
    }
}
