package com.bankerisemock.product.repository;

import com.bankerisemock.product.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


@Transactional
public interface ProductRepository extends JpaRepository<Product,Integer> {
    public boolean existsProductByCode(String code);
    public boolean existsProductByName(String name);
}
