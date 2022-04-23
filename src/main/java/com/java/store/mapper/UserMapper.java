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
        res.setPhoneNumber(userDto.getPhoneNumber());
        return res;
    }

    @Override
    public UserDto EntityToDto(Users user) {
        UserDto res = new UserDto();
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
