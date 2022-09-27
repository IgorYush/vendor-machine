package com.vendor.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(schema = "vendor", name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            name = "id",
            updatable = false,
            nullable = false
    )
    private UUID id;

    @Column(name = "cost")
    private Integer cost;

    @Column(name = "name")
    private String name;

    @Column(name = "creator_id")
    private UUID creatorId;

    public Product(Integer cost, String name) {
        this.cost = cost;
        this.name = name;
    }
}
