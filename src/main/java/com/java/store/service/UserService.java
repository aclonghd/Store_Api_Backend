package com.java.store.service;

import com.java.store.dto.request.UserRequest;
import com.java.store.dto.response.UserResponse;
import com.java.store.exception.ServiceException;
import com.java.store.mapper.UserMapper;
import com.java.store.module.Users;
import com.java.store.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static org.springframework.http.HttpStatus.*;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("Username or password is incorrect!");
        }
        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    public UserResponse getUserInfoByUsername(String username){
        Users user = userRepo.findByUsername(username);
        if(user == null){
            throw new ServiceException(BAD_REQUEST.value(),BAD_REQUEST.toString());
        }

        return userMapper.EntityToDto(user);
    }

    public void register(UserRequest userRequest) {
        if(userRepo.findByUsername(userRequest.getUsername()) != null){
            throw new ServiceException(BAD_REQUEST.value(),"Username has already been taken!");
        }
        Users user = userMapper.DtoToEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepo.save(user);
    }

    public void updateUserInformation(UserRequest userResponse) {
        Users userOrigin = userRepo.findByUsername(userResponse.getUsername());
        if(userOrigin == null) throw new ServiceException(BAD_REQUEST.value(),BAD_REQUEST.toString());
        userOrigin.setFirstName(userResponse.getFirstName());
        userOrigin.setLastName(userResponse.getLastName());
        userOrigin.setAddress(userResponse.getAddress());
        userOrigin.setAge(userResponse.getAge());
        userOrigin.setEmail(userResponse.getEmail());
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

    public List<UserResponse> getAllUserInfo(){
        List<Users> all = userRepo.findAll();
        return all.stream().map(userMapper::EntityToDto).collect(Collectors.toList());
    }

    public List<String> getDiscountCodeByUsername(String username){
        return userRepo.findDiscountByUsername(username);
    }
}
