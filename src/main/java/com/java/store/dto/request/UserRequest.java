package com.java.store.dto.request;

import com.java.store.dto.response.UserResponse;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserRequest extends UserResponse {
    @NotNull @NotBlank
    private String password;
}
