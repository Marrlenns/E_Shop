package com.example.e_shop.services.impl;

import com.example.e_shop.dto.product.ProductDetailResponse;
import com.example.e_shop.dto.product.ProductRequest;
import com.example.e_shop.dto.product.ProductResponse;
import com.example.e_shop.entities.Product;
import com.example.e_shop.entities.User;
import com.example.e_shop.enums.Role;
import com.example.e_shop.enums.Type;
import com.example.e_shop.exceptions.BadCredentialsException;
import com.example.e_shop.exceptions.BadRequestException;
import com.example.e_shop.mappers.ProductMapper;
import com.example.e_shop.repositories.ProductRepository;
import com.example.e_shop.repositories.UserRepository;
import com.example.e_shop.services.AuthService;
import com.example.e_shop.services.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AuthService authService;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;

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
            throw new BadRequestException("Product with this code is already exist! Try other code!");
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

    @Override
    public List<ProductResponse> all(String token) {
        User user = authService.getUserFromToken(token);
        if(user.getRole().equals(Role.ADMIN))
            return productMapper.toDtos(productRepository.findAll());
        return productMapper.toDtos(productRepository.findAllByAgeAccessLessThanEqual(user.getClient().getAge()));
    }

    public void detachUser(Product product){
        List<Product> products = product.getClient().getProducts();
        List<Product> newProducts = new ArrayList<>();
        for(Product product1: products)
            if(!Objects.equals(product.getId(), product1.getId()))
                newProducts.add(product1);
        User user = product.getClient().getUser();
        user.getClient().setProducts(newProducts);
        userRepository.save(user);
        product.setClient(null);
    }

    @Override
    public void delete(String token, String code) {
        User user = authService.getUserFromToken(token);
        if(!user.getRole().equals(Role.ADMIN))
            throw new BadCredentialsException("U can't delete products!");
        Optional<Product> product = productRepository.findByCode(code);
        if(product.isEmpty())
            throw new BadRequestException("This product doesn't exist!");
        if(product.get().getClient() != null)
            detachUser(product.get());
        productRepository.delete(product.get());
    }

    @Override
    public ProductDetailResponse findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty())
            throw new BadRequestException("Product with id: " + id + " - doesn't exist");
        return productMapper.toDetailDto(product.get());
    }

    @Override
    public ProductDetailResponse updateByCode(String token, String code, ProductRequest request) {
        if(!authService.getUserFromToken(token).getRole().equals(Role.ADMIN))
            throw new BadCredentialsException("Only admins can update products!");
        if(productRepository.findByCode(code).isEmpty())
            throw new BadRequestException("Product doesn't exist!");
        Product product = productRepository.findByCode(code).get();
        product.setTitle(request.getTitle());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setType(Type.valueOf(request.getType()));
        product.setCode(request.getCode());
        product.setAgeAccess(request.getAgeAccess());
        productRepository.save(product);
        return productMapper.toDetailDto(product);
    }
}
