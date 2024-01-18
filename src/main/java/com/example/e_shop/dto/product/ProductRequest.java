package com.example.e_shop.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String title;
    private Integer price;
    private String description;
    private String type;
    private String code;
    private Integer ageAccess;
}
