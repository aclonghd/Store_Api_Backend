package com.java.store.controller;

import static org.springframework.http.HttpStatus.*;

import com.java.store.dto.request.UserRequest;
import com.java.store.dto.response.ResponseDto;
import com.java.store.dto.response.UserResponse;
import com.java.store.enums.Role;
import com.java.store.exception.ServiceException;
import com.java.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "users")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "api/register")
    public ResponseEntity<Object> register(@RequestBody @Valid UserRequest user){
            userService.register(user);
            ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString());
            return new ResponseEntity<>(responseDto, OK);

    }

    @GetMapping(path = "user-info")
    public ResponseEntity<Object> getUserInformation(@RequestParam @NotBlank String username, Authentication authentication){
        if(!authentication.getPrincipal().equals(username)) {
            if(!authentication.getAuthorities().toArray()[0].toString().equals(Role.ADMIN.name()))
                throw new ServiceException(BAD_REQUEST.value(),BAD_REQUEST.toString());
        }
        UserResponse user = userService.getUserInfoByUsername(username);
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(), user);
        return new ResponseEntity<>(responseDto, OK);

    }

    @PostMapping(path = "user-info")
    public ResponseEntity<Object> updateUserInformation(@RequestBody @Valid UserRequest userRequest, Authentication authentication){
        if(!authentication.getPrincipal().equals(userRequest.getUsername())) {
            if(!authentication.getAuthorities().toArray()[0].toString().equals(Role.ADMIN.name()))
                throw new ServiceException(BAD_REQUEST.value(),BAD_REQUEST.toString());
        }
        userService.updateUserInformation(userRequest);
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString());
        return new ResponseEntity<>(responseDto, OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserInfo(){
        ResponseDto responseDto = new ResponseDto(OK.value(), OK.toString(), userService.getAllUserInfo());
        return new ResponseEntity<>(responseDto, OK);
    }

    @GetMapping(path = "user-discount")
    public ResponseEntity<Object> getDiscountCodeOfUser(@RequestParam @NotBlank String username, Authentication authentication){
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

    @PostMapping(path = "change-password")
    public ResponseEntity<Object> changePassword(@RequestBody @NotNull Map<String, String> map, Authentication authentication){
        String username = authentication.getPrincipal().toString();
        if(userService.changePassword(map.get("oldPassword"), map.get("newPassword"), username))
            return new ResponseEntity<>(new ResponseDto(OK.value(), OK.toString()), OK);
        return new ResponseEntity<>(new ResponseDto(NO_CONTENT.value(), NO_CONTENT.toString()), OK);
    }
}

