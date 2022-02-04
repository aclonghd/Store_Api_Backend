package com.java.store.controller;

import static org.springframework.http.HttpStatus.*;

import com.java.store.dto.ResponseDto;
import com.java.store.dto.UserDto;
import com.java.store.enums.Role;
import com.java.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> getUserInformation(@RequestBody String username, Authentication authentication){
        try {
            if(!authentication.getPrincipal().equals(username)) {
                if(!authentication.getAuthorities().toArray()[0].toString().equals(Role.ADMIN.name()))
                    throw new Exception(BAD_REQUEST.toString());
            }
            UserDto user = userService.getUserInfoByUsername(username);
            ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(), user);
            return new ResponseEntity<>(responseDto, OK);
        } catch (Exception ex) {
            ResponseDto responseDto = new ResponseDto(BAD_REQUEST.value(), BAD_REQUEST.toString());
            return new ResponseEntity<>(responseDto, BAD_REQUEST);
        }
    }

    @PostMapping(path = "user-info")
    public ResponseEntity<Object> updateUserInformation(@RequestBody UserDto userDto, Authentication authentication){
        try {
            if(!authentication.getPrincipal().equals(userDto.getUsername())) {
                if(!authentication.getAuthorities().toArray()[0].toString().equals(Role.ADMIN.name()))
                    throw new Exception(BAD_REQUEST.toString());
            }
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

    @GetMapping(path = "user-discount")
    public ResponseEntity<Object> getDiscountCodeOfUser(@RequestParam String username, Authentication authentication){
        if(!authentication.getPrincipal().equals(username)) {
            if(!authentication.getAuthorities().toArray()[0].toString().equals(Role.ADMIN.name()))
                return new ResponseEntity<>(new ResponseDto(BAD_REQUEST.value(), BAD_REQUEST.toString()), OK);
        }
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(),userService.getDiscountCodeByUsername(username));
        return new ResponseEntity<>(responseDto, OK);
    }

    @GetMapping(path = "admin-authentication")
    public ResponseEntity<Object> adminAuthentication(Authentication authentication){
        return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString()), OK);
    }
}

