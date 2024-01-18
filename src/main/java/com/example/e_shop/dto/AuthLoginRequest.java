package com.example.e_shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginRequest {
    private String nickname;
    private String password;
}
