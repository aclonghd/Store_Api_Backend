package com.java.store.module;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Users user;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
    private float reviewScore;
    private String review;
    private Timestamp timeStamp;
    @ManyToOne
    @JoinColumn(name = "parentId", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Review reviewParent;
}
