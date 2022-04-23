package com.java.store.module;

import com.java.store.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String email;
    private int age;
    private String address;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @Column(nullable = false)
    private String role;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(joinColumns =  @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private Set<Discount> discounts;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        switch (role){
            case "ADMIN":
                return Role.ADMIN.getGrantedAuthorities();
            case "USER":
                return Role.USER.getGrantedAuthorities();
        }
        return null;
    }
}
