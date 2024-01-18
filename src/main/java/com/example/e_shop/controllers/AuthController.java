package com.example.e_shop.controllers;

import com.example.e_shop.dto.AuthLoginRequest;
import com.example.e_shop.dto.AuthLoginResponse;
import com.example.e_shop.dto.UserRegisterRequest;
import com.example.e_shop.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody UserRegisterRequest userRegisterRequest){
        authService.register(userRegisterRequest);
        return "User: " + userRegisterRequest.getNickname() +  " - added successfully!";
    }

    @PostMapping("/login")
    public AuthLoginResponse login(@RequestBody AuthLoginRequest authLoginRequest){
        return authService.login(authLoginRequest);
    }
}
