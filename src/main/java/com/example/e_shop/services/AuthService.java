package com.example.e_shop.services;

import com.example.e_shop.dto.AuthLoginRequest;
import com.example.e_shop.dto.AuthLoginResponse;
import com.example.e_shop.dto.UserRegisterRequest;
import com.example.e_shop.entities.User;

public interface AuthService {
    void register(UserRegisterRequest userRegisterRequest);

    AuthLoginResponse login(AuthLoginRequest authLoginRequest);

    public User getUserFromToken(String token);
}
