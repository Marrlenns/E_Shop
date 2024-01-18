package com.example.e_shop.services.impl;

import com.example.e_shop.config.JwtService;
import com.example.e_shop.dto.AuthLoginRequest;
import com.example.e_shop.dto.AuthLoginResponse;
import com.example.e_shop.dto.UserRegisterRequest;
import com.example.e_shop.entities.Client;
import com.example.e_shop.entities.User;
import com.example.e_shop.enums.Role;
import com.example.e_shop.exceptions.BadCredentialsException;
import com.example.e_shop.repositories.UserRepository;
import com.example.e_shop.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private  UserRepository userRepository;
    private  PasswordEncoder encoder;
    private final JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @Override
    public void register(UserRegisterRequest userRegisterRequest) {
        if (userRepository.findByNickname(userRegisterRequest.getNickname()).isPresent())
            throw new BadCredentialsException("User with Nickname: " + userRegisterRequest.getNickname() + " is already exist!");

        User user = new User();
        user.setNickname(userRegisterRequest.getNickname());
        user.setPassword(encoder.encode(userRegisterRequest.getPassword()));
        if (userRegisterRequest.getRole()==null)
            user.setRole(Role.ADMIN);
        else {
            Client client = new Client();
            client.setAge(userRegisterRequest.getAge());
            client.setFullName(userRegisterRequest.getFullName());
            client.setUser(user);
            user.setClient(client);
            user.setRole(Role.valueOf(userRegisterRequest.getRole()));
        }

        userRepository.save(user);

    }

    @Override
    public AuthLoginResponse login(AuthLoginRequest authLoginRequest) {

        Optional<User> user = userRepository.findByNickname(authLoginRequest.getNickname());
        if (user.isEmpty())
            throw new BadCredentialsException("User not found!");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authLoginRequest.getNickname(),authLoginRequest.getPassword()));
        }catch (org.springframework.security.authentication.BadCredentialsException e){
            throw new BadCredentialsException("User not found");
        }

        return convertToResponse(user);
    }

    private AuthLoginResponse convertToResponse(Optional<User> user) {
        AuthLoginResponse authLoginResponse = new AuthLoginResponse();
        authLoginResponse.setNickname(user.get().getNickname());
        authLoginResponse.setId(user.get().getId());
        if (user.get().getRole().equals(Role.CLIENT))
            authLoginResponse.setFullName(user.get().getClient().getFullName());
        Map<String, Object> extraClaims = new HashMap<>();

        String token = jwtService.generateToken(extraClaims, user.get());
        authLoginResponse.setToken(token);

        return authLoginResponse;
    }

    @Override
    public User getUserFromToken(String token){

        String[] chunks = token.substring(7).split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        JSONParser jsonParser = new JSONParser();
        JSONObject object = null;
        try {
            object = (JSONObject) jsonParser.parse(decoder.decode(chunks[1]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return userRepository.findByNickname(String.valueOf(object.get("sub"))).orElseThrow(() -> new RuntimeException("User can be null"));
    }
}
