package com.java.store.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
import static com.java.store.enums.Permission.*;



public enum Role {
    ADMIN(Set.of(
            CART_READ, CART_ADD, CART_WRITE, CART_DELETE,
            USER_READ, USER_ADD, USER_WRITE, USER_DELETE,
            PRODUCT_READ, PRODUCT_ADD, PRODUCT_WRITE, PRODUCT_DELETE)),
    USER(Set.of(
            CART_READ, CART_ADD, CART_WRITE, CART_DELETE,
            USER_READ, USER_ADD, USER_WRITE,
            PRODUCT_READ));

    private final Set<Permission> permissionSet;

    Role(Set<Permission> permissionSet) {
        this.permissionSet = permissionSet;
    }


    public Set<Permission> getPermissionSet(){
        return permissionSet;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        return getPermissionSet().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
    }
}
