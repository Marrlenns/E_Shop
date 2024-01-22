package com.example.e_shop.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailResponse {
    private Long id;
    private String nickname;
    private String fullName;
    private Integer age;
}
