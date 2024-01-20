package com.example.e_shop.controllers;

import com.example.e_shop.repositories.ClientRepository;
import com.example.e_shop.repositories.ProductRepository;
import com.example.e_shop.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/client")
public class ClientController {

    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final ClientService clientService;

    @PostMapping("/buy")
    public String buy(@RequestHeader("Authorization") String token, @RequestParam String code){
        clientService.buy(token, code);
        return "You have successfully bought a " + productRepository.findByCode(code).get().getTitle();
    }
}
