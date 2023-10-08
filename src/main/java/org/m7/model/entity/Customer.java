package org.m7.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
    private Set<Purchase> purchases;
}
