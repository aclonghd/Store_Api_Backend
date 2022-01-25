package com.java.store.controller;

import static org.springframework.http.HttpStatus.*;

import com.java.store.dto.ResponseDto;
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
    public ResponseEntity<Object> register(@RequestBody UserDto user){
        try {
            userService.register(user);
            ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString());
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception ex){
            ResponseDto responseDto = new ResponseDto(BAD_REQUEST.value(), ex.getMessage());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }

    @GetMapping(path = "user-info")
    public ResponseEntity<Object> getUserInformation(@RequestBody String username){
        try {
            UserDto user = userService.getUserInfoByUsername(username);
            ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(), user);
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception ex) {
            ResponseDto responseDto = new ResponseDto(BAD_REQUEST.value(), BAD_REQUEST.toString());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }

    @PostMapping(path = "user-info")
    public ResponseEntity<Object> updateUserInformation(@RequestBody UserDto userDto){
        try {
            userService.updateUserInformation(userDto);
            ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString());
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception ex){
            ResponseDto responseDto = new ResponseDto(BAD_REQUEST.value(), ex.getMessage());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserInfo(){
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(), userService.getAllUserInfo());
        return new ResponseEntity<>(responseDto, OK);
    }
}

