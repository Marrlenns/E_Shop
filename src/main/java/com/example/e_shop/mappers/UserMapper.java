package com.example.e_shop.mappers;

import com.example.e_shop.dto.user.UserDetailResponse;
import com.example.e_shop.dto.user.UserResponse;
import com.example.e_shop.entities.User;

import java.util.List;

public interface UserMapper {
    List<UserResponse> toDtos(List<User> users);

    UserDetailResponse toDetailDto(User user);
}
