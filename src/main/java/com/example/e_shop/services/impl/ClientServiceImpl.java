package com.example.e_shop.services.impl;

import com.example.e_shop.entities.Product;
import com.example.e_shop.entities.User;
import com.example.e_shop.enums.Role;
import com.example.e_shop.exceptions.BadCredentialsException;
import com.example.e_shop.exceptions.BadRequestException;
import com.example.e_shop.repositories.ProductRepository;
import com.example.e_shop.repositories.UserRepository;
import com.example.e_shop.services.AuthService;
import com.example.e_shop.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final AuthService authService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public void buy(String token, String code) {
        User user = authService.getUserFromToken(token);
        if(user.getRole().equals(Role.ADMIN))
            throw new BadCredentialsException("Only clients can buy products!");
        Optional<Product> product = productRepository.findByCode(code);
        if(product.isEmpty())
            throw new BadRequestException("Product with this code doesn't exist! Try other code!");
        if(product.get().getClient() != null && product.get().getClient() != user.getClient())
            throw new BadRequestException("This product already selled!");
        if(product.get().getClient() != null)
            throw new BadRequestException("You already bought this product!");
        List<Product> products = new ArrayList<>();
        if(!user.getClient().getProducts().isEmpty())
            products = user.getClient().getProducts();
        products.add(product.get());
        user.getClient().setProducts(products);
        userRepository.save(user);
        product.get().setClient(user.getClient());
        productRepository.save(product.get());
    }
}
