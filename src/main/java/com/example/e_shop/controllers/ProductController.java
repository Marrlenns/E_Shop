package com.example.e_shop.controllers;

import com.example.e_shop.dto.product.ProductDetailResponse;
import com.example.e_shop.dto.product.ProductRequest;
import com.example.e_shop.dto.product.ProductResponse;
import com.example.e_shop.exceptions.BadCredentialsException;
import com.example.e_shop.mappers.ProductMapper;
import com.example.e_shop.repositories.ProductRepository;
import com.example.e_shop.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping()
    public String hello(){
        return "marlen salam";
    }

    @PostMapping("/add")
    public String add(@RequestBody ProductRequest productRequest, @RequestHeader("Authorization") String token){
        productService.add(productRequest, token);
        return "Product: " + productRequest.getTitle() + " - added successfully!";
    }

    @GetMapping("/all")
//    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public List<ProductResponse> all(@RequestHeader("Authorization") String token){
        System.out.println("the token"+token);
        if(token.isEmpty())
            throw new BadCredentialsException("U can't view this page!");
        return productService.all(token);
    }

    @GetMapping("/{id}")
    public ProductDetailResponse detail(@PathVariable Long id){
        return productService.findById(id);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestHeader("Authorization") String token, @RequestParam  String code){
        productService.delete(token, code);
        return "Product deleted successfully";
    }

    @PutMapping("/update")
    public ProductDetailResponse update(@RequestHeader("Authorization") String token, @RequestParam String code, @RequestBody ProductRequest request){
        return productService.updateByCode(token, code, request);
    }

}
