package com.example.e_shop.repositories;

import com.example.e_shop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByCode(String code);
    Optional<Product> findByTitle(String title);
    List<Product> findAllByAgeAccessLessThanEqual(Integer ageAccess);
}
