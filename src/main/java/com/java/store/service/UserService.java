package com.java.store.service;

import com.java.store.dto.UserDto;
import com.java.store.mapper.UserMapper;
import com.java.store.module.Users;
import com.java.store.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Username or password is incorrect!");
        }
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public UserDto getUserInfoByUsername(String username) throws Exception{
        Users user = userRepo.findByUsername(username);
        if(user == null){
            throw new Exception("Bad Request");
        }

        return userMapper.EntityToDto(user);
    }

    public void register(UserDto userDto) throws Exception{
        if(userRepo.findByUsername(userDto.getUsername()) != null){
            throw new Exception("Username has already been taken!");
        }
        Users user = userMapper.DtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepo.save(user);
    }

    public void updateUserInformation(UserDto userDto) throws Exception{
        Users userOrigin = userRepo.findByUsername(userDto.getUsername());
        if(userOrigin == null) throw new Exception("Bad Request");
        userOrigin.setFirstName(userDto.getFirstName());
        userOrigin.setLastName(userDto.getLastName());
        userOrigin.setAddress(userDto.getAddress());
        userOrigin.setAge(userDto.getAge());
        userOrigin.setEmail(userDto.getEmail());
        userRepo.save(userOrigin);
    }

    public boolean changePassword(String oldPassword, String newPassword, String username){
        Users user = userRepo.findByUsername(username);
        if(passwordEncoder.matches(oldPassword, user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public List<UserDto> getAllUserInfo(){
        List<Users> all = userRepo.findAll();
        return all.stream().map(userMapper::EntityToDto).collect(Collectors.toList());
    }

    public List<String> getDiscountCodeByUsername(String username){
        return userRepo.findDiscountByUsername(username);
    }
}
