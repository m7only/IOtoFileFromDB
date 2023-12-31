package org.m7.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * Сущность товара
 */
@Entity
@Table(name = "products")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private Integer price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Purchase> purchases;

    public Product(String productName, Long price) {
        this.productName = productName;
        this.price = price.intValue();
    }
}
