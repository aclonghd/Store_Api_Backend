package com.java.store.module;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
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

    @OneToMany
    private Set<Product> products;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false, referencedColumnName="id")
    private Users user;

    private long totalPrice;

}
