package com.java.store.dto.response;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class UserResponse {
    @NotNull @NotBlank
    private String username;
    @Email
    private String email;
    private int age;
    private String address;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
}
