package com.example.e_shop.services.impl;

import com.example.e_shop.dto.UserRegisterRequest;
import com.example.e_shop.dto.user.PasswordRequest;
import com.example.e_shop.dto.user.UserDetailResponse;
import com.example.e_shop.dto.user.UserResponse;
import com.example.e_shop.entities.Client;
import com.example.e_shop.entities.Product;
import com.example.e_shop.entities.User;
import com.example.e_shop.enums.Role;
import com.example.e_shop.exceptions.BadCredentialsException;
import com.example.e_shop.exceptions.BadRequestException;
import com.example.e_shop.mappers.UserMapper;
import com.example.e_shop.repositories.ClientRepository;
import com.example.e_shop.repositories.ProductRepository;
import com.example.e_shop.repositories.UserRepository;
import com.example.e_shop.services.AuthService;
import com.example.e_shop.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthService authService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final UserMapper clientMapper;
    private final ClientRepository clientRepository;
    private final PasswordEncoder encoder;

    @Override
    public void buy(String token, String code) {
        User user = authService.getUserFromToken(token);
        if(user.getRole().equals(Role.ADMIN))
            throw new BadCredentialsException("Only clients can buy products!");
        Optional<Product> product = productRepository.findByCode(code);
        if(product.isEmpty())
            throw new BadRequestException("Product with this code doesn't exist! Try other code!");
        if(user.getClient().getAge() < product.get().getAgeAccess())
            throw new BadRequestException("You're not allowed to buy this product!");
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

    @Override
    public List<UserResponse> all(String token) {
        if(!authService.getUserFromToken(token).getRole().equals(Role.ADMIN))
            throw new BadCredentialsException("This operation available only for admins!");
        List<User> users = userRepository.findAll();
        return clientMapper.toDtos(users);
    }

    @Override
    public UserDetailResponse findById(String token, Long id) {
        if(!authService.getUserFromToken(token).getRole().equals(Role.ADMIN))
            throw new BadCredentialsException("This operation available only for admins!");
        Optional<Client> client = clientRepository.findById(id);
        if(client.isEmpty())
            throw new BadRequestException("Client with this id doesn't exist!");
        return clientMapper.toDetailDto(client.get().getUser());
    }

    @Override
    public void update(String token, UserRegisterRequest request) {
        User user = authService.getUserFromToken(token);
        if(user.getRole().equals(Role.ADMIN)){
            user.setNickname(request.getNickname());
        } else{
            user.setNickname(request.getNickname());
            user.getClient().setFullName(request.getFullName());
            user.getClient().setAge(request.getAge());
        }
        userRepository.save(user);
    }

    @Override
    public void updatePassword(String token, PasswordRequest request) {
        User user = authService.getUserFromToken(token);
        String password = user.getPassword();
        String oldPassword = request.getOldPassword();
        String newPassword1 = request.getNewPassword1();
        String newPassword2 = request.getNewPassword2();
        if(!Objects.equals(newPassword2, newPassword1))
            throw new BadRequestException("Passwords aren't match!");
        if(!encoder.matches(oldPassword, password))
            throw new BadRequestException("Wrong old password!");
        user.setPassword(encoder.encode(newPassword1));
        userRepository.save(user);
    }
}
