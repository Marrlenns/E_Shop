package com.example.e_shop.mappers.impl;

import com.example.e_shop.dto.user.UserDetailResponse;
import com.example.e_shop.dto.user.UserResponse;
import com.example.e_shop.entities.User;
import com.example.e_shop.enums.Role;
import com.example.e_shop.mappers.UserMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapperImpl implements UserMapper {

    public UserResponse toDto(User user){
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFullName(user.getClient().getFullName());
        return response;
    }

    @Override
    public List<UserResponse> toDtos(List<User> users) {
        List<UserResponse> responses = new ArrayList<>();
        for(User user: users)
            if(!user.getRole().equals(Role.ADMIN))
                responses.add(toDto(user));

        return responses;
    }

    @Override
    public UserDetailResponse toDetailDto(User user) {
        UserDetailResponse response = new UserDetailResponse();
        response.setId(user.getId());
        response.setFullName(user.getClient().getFullName());
        response.setNickname(user.getNickname());
        response.setAge(user.getClient().getAge());
        return response;
    }
}
