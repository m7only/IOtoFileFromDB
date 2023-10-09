package org.m7.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Сущность покупателя
 */
@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Purchase> purchases;

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customer(String firstName, String lastName, List<Purchase> purchases) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.purchases = purchases;
    }
}
