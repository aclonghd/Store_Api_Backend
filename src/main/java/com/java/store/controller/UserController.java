package com.java.store.controller;

import static org.springframework.http.HttpStatus.*;
import com.java.store.dto.UserDto;
import com.java.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    @PostMapping(path = "api/register")
    public ResponseEntity<String> register(@RequestBody UserDto user){
        try {
            userService.register(user);
        } catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), OK);
        }
        return new ResponseEntity<>("200", OK);
    }

    @GetMapping(path = "user-info")
    public ResponseEntity<UserDto> getUserInformation(@RequestBody String username){
        try {
            UserDto user = userService.getUserInfoByUsername(username);
            return new ResponseEntity<>(user, OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @PostMapping(path = "user-info")
    public ResponseEntity<UserDto> updateUserInformation(@RequestBody UserDto userDto){
        try {
            userService.updateUserInformation(userDto);
            return new ResponseEntity<>(userDto, OK);
        } catch (Exception ex){
            return new ResponseEntity<>(BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUserInfo(){
        return new ResponseEntity<>(userService.getAllUserInfo(),OK);
    }
}

