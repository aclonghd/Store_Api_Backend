package com.java.store.module;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GenericGenerator(name = "card_generator_id", strategy = "com.java.store.GeneratorCartId")
    @GeneratedValue(generator = "card_generator_id")
    private String id;

    @ElementCollection
    @CollectionTable(name = "carts_products",
            joinColumns = @JoinColumn(name = "cart_id", referencedColumnName = "id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "amount")
    private Map<Product, Integer> products;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false, referencedColumnName="id")
    private Users user;

    private long totalPrice;

}
