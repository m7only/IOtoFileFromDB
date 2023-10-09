package org.m7.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Сущность покупки
 */
@Entity
@Table(name = "purchases")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime purchaseDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product product;

//    c.lastName, c.firstName, pr.productName, sum(pr.price)


    public Purchase(Customer customer, Product product) {
        this.customer = customer;
        this.product = product;
    }

    public Purchase(Product product) {
        this.product = product;
    }
}
