package com.example.e_shop.dto.product;

import com.example.e_shop.entities.Client;
import com.example.e_shop.enums.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailResponse {
    private Long id;
    private String title;
    private Integer price;
    private String description;
    private String type;
    private String code;
    private Integer ageAccess;
    private String clientName;
}
