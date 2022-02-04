package com.java.store.module;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private long price;
    private String information;
    private int quantity;
    private String color;
    @ElementCollection
    @CollectionTable(name = "image_url_table_product",
        joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id")
    )
    private Set<String> imageUrl;
}
