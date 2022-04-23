package com.java.store.module;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import java.sql.Timestamp;
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
            joinColumns = @JoinColumn(name = "cart_id", referencedColumnName = "id"),
            foreignKey = @ForeignKey(foreignKeyDefinition = "foreign key (cart_id) references carts (id) on delete cascade")
    )
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "amount")
    private Map<Product, Integer> products;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false, referencedColumnName="id")
    private Users user;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private Set<Discount> discountApply;
    private float totalPrice;
    private Timestamp timestamp;
    private String paymentMethods;
    private String appointmentDate;
    private String status;
}
