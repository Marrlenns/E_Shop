package com.example.e_shop.controllers;

import com.example.e_shop.dto.UserRegisterRequest;
import com.example.e_shop.dto.user.PasswordRequest;
import com.example.e_shop.dto.user.UserDetailResponse;
import com.example.e_shop.dto.user.UserResponse;
import com.example.e_shop.repositories.ProductRepository;
import com.example.e_shop.services.AuthService;
import com.example.e_shop.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final AuthService authService;
    private PasswordEncoder encoder;

    @PostMapping("/buy")
    public String buy(@RequestHeader("Authorization") String token, @RequestParam String code){
        userService.buy(token, code);
        return "You have successfully bought a " + productRepository.findByCode(code).get().getTitle();
    }

    @GetMapping("/all")
    public List<UserResponse> all(@RequestHeader("Authorization") String token){
        return userService.all(token);
    }

    @GetMapping("/{id}")
    public UserDetailResponse detail(@RequestHeader("Authorization") String token, @PathVariable Long id){
        return userService.findById(token, id);
    }

    @PutMapping("/update")
    public String update(@RequestHeader("Authorization") String token, @RequestBody UserRegisterRequest request){
        userService.update(token, request);
        return "All changes have been made!";
    }

    @PutMapping("/update/password")
    public String update(@RequestHeader("Authorization") String token, @RequestBody PasswordRequest request){
        userService.updatePassword(token, request);
        return "Password changed successfully!";
    }
}
