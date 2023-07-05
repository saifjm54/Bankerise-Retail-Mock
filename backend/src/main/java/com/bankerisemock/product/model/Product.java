package com.bankerisemock.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Id
    private Integer id;
    @Column(
            nullable = false
    )
    private String code;
    @Column(
            nullable = false
    )
    private String name;

}
