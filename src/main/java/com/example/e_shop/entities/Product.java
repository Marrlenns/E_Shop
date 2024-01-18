package com.example.e_shop.entities;

import com.example.e_shop.enums.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer price;
    private String description;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String code;
    private Integer ageAccess;

    @ManyToOne
    private Client client;

}
