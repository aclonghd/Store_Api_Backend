package com.java.store.module;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Users user;
    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;
    private float reviewScore;
    private String review;
    private Long parentId;
}
