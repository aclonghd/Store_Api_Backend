package com.java.store.enums;

public enum Permission {
    PRODUCT_READ("PRODUCT_READ"), PRODUCT_WRITE("PRODUCT_WRITE"), PRODUCT_ADD("PRODUCT_ADD"), PRODUCT_DELETE("PRODUCT_DELETE"),
    USER_READ("USER_READ"), USER_WRITE("USER_WRITE"), USER_ADD("USER_ADD"), USER_DELETE("USER_DELETE"),
    CART_READ("CART_READ"), CART_WRITE("CART_WRITE"), CART_ADD("CART_ADD"), CART_DELETE("CART_DELETE");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }
}
