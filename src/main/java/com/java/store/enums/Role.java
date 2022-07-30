package com.java.store.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import static com.java.store.enums.Permission.*;



public enum Role {
    ADMIN(Set.of(
            CART_READ, CART_ADD, CART_WRITE, CART_DELETE,
            USER_READ, USER_ADD, USER_WRITE, USER_DELETE,
            PRODUCT_READ, PRODUCT_ADD, PRODUCT_WRITE, PRODUCT_DELETE,
            DISCOUNT_READ, DISCOUNT_WRITE, DISCOUNT_ADD, DISCOUNT_DELETE,
            REVIEW_READ, REVIEW_WRITE, REVIEW_ADD, REVIEW_DELETE), "ROLE_ADMIN"),
    USER(Set.of(
            CART_READ, CART_ADD, CART_WRITE, CART_DELETE,
            USER_READ, USER_ADD, USER_WRITE,
            PRODUCT_READ, DISCOUNT_READ, REVIEW_READ, REVIEW_ADD), "ROLE_USER");
    private final Set<Permission> permissionSet;
    private final String role;
    Role(Set<Permission> permissionSet, String role) {
        this.permissionSet = permissionSet;
        this.role = role;
    }
    public Set<Permission> getPermissionSet(){
        return permissionSet;
    }
    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> authorities = getPermissionSet().stream().map(permission -> new SimpleGrantedAuthority(permission.name())).collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }
}
