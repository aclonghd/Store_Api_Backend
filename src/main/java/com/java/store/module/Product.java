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
    private float price;
    @Column(columnDefinition="text")
    private String information;
    private int quantity;
    private String color;
    @ElementCollection
    @CollectionTable(name = "image_url_table_product",
        joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"), foreignKey = @ForeignKey(foreignKeyDefinition = "foreign key (product_id) references products (id) on delete cascade")
    )
    private Set<String> imageUrl;
    private String mainImage;
    private float averageRatting;
    private int voteNumber;
    private String titleUrl;

    @ManyToMany
    @JoinTable(joinColumns =  @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tags> tags;

}
