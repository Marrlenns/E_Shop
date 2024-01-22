package com.example.e_shop.services;

import com.example.e_shop.dto.UserRegisterRequest;
import com.example.e_shop.dto.user.PasswordRequest;
import com.example.e_shop.dto.user.UserDetailResponse;
import com.example.e_shop.dto.user.UserResponse;

import java.util.List;

public interface UserService {
    void buy(String token, String code);

    List<UserResponse> all(String token);

    UserDetailResponse findById(String token, Long id);

    void update(String token, UserRegisterRequest request);

    void updatePassword(String token, PasswordRequest request);
}
