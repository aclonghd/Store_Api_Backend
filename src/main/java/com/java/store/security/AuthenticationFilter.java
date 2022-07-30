package com.java.store.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.java.store.JWTConfig;
import com.java.store.dto.response.ResponseDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JWTConfig jwtConfig;
    private final String type;
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AuthenticationRequest user = objectMapper.readValue(request.getInputStream(), AuthenticationRequest.class);
            Authentication result = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            if(type.equals("manager")){
                if(result.getAuthorities().size() != 21){
                    return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), null));
                }
            }
            return result;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        int size = user.getAuthorities().size();
        String role = "USER";
        if(size == 21) role = "ADMIN";
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withClaim("roles", role)
                .sign(Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes()));
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getTokenExpirationAfterDays() * 24 * 3600 * 1000))
                .withIssuer(request.getPathInfo())
                .withClaim("roles", role)
                .sign(Algorithm.HMAC256(jwtConfig.getSecretKey().getBytes()));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        List<Map<String, String>> res = new ArrayList<>();
        res.add(tokens);
        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.value(), HttpStatus.OK.toString(), res);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writeValue(response.getOutputStream(), responseDto);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.value(), "User or password is incorrect!");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writeValue(response.getOutputStream(), responseDto);
    }
}

@Data
class AuthenticationRequest{
    private String username;
    private String password;
}
