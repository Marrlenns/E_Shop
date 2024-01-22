package com.example.e_shop.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRequest {
    private String oldPassword;
    private String newPassword1;
    private String newPassword2;
}
