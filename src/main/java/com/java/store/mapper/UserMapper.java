package com.java.store.mapper;

import com.java.store.dto.UserDto;
import com.java.store.module.Users;

public class UserMapper implements BaseMapper<UserDto, Users> {
    @Override
    public Users DtoToEntity(UserDto userDto) {
        Users res = new Users();
        res.setUsername(userDto.getUsername());
        res.setAge(userDto.getAge());
        res.setEmail(userDto.getEmail());
        res.setAddress(userDto.getAddress());
        res.setFirstName(userDto.getFirstName());
        res.setLastName(userDto.getLastName());
        res.setRole(userDto.getRole());
        return res;
    }

    @Override
    public UserDto EntityToDto(Users users) {
        UserDto res = new UserDto();
        res.setUsername(users.getUsername());
        res.setAge(users.getAge());
        res.setEmail(users.getEmail());
        res.setAddress(users.getAddress());
        res.setFirstName(users.getFirstName());
        res.setLastName(users.getLastName());
        res.setRole(users.getRole());
        return res;
    }
}
