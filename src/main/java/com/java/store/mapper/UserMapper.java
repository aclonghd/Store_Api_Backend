package com.java.store.mapper;

import com.java.store.dto.response.UserResponse;
import com.java.store.enums.Role;
import com.java.store.module.Users;

public class UserMapper implements BaseMapper<UserResponse, Users> {
    @Override
    public Users DtoToEntity(UserResponse userResponse) {
        Users res = new Users();
        res.setUsername(userResponse.getUsername());
        res.setAge(userResponse.getAge());
        res.setEmail(userResponse.getEmail());
        res.setAddress(userResponse.getAddress());
        res.setFirstName(userResponse.getFirstName());
        res.setLastName(userResponse.getLastName());
        res.setRole(Role.USER.toString());
        res.setPhoneNumber(userResponse.getPhoneNumber());
        return res;
    }

    @Override
    public UserResponse EntityToDto(Users user) {
        UserResponse res = new UserResponse();
        res.setUsername(user.getUsername());
        res.setAge(user.getAge());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setRole(user.getRole());
        res.setPhoneNumber(user.getPhoneNumber());
        return res;
    }
}
