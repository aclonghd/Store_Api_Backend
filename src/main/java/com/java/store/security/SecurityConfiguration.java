package com.java.store.security;

import static org.springframework.http.HttpMethod.*;
import static com.java.store.enums.Role.*;

import com.java.store.JWTConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTConfig jwtConfig;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean(), jwtConfig);
        authenticationFilter.setFilterProcessesUrl("/users/api/login");

        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(authenticationFilter)
                .addFilterBefore(new AuthorizationCustomFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(GET, "/product/api/**").permitAll()
                .antMatchers(POST, "/users/api/register", "/users/api/login").permitAll()
                .antMatchers(GET,"/users/user-info").hasAuthority(USER.name())
                .antMatchers(POST,"/users/user-info").hasAuthority(USER.name())
                .antMatchers(GET,"/users/user-info").hasAuthority(ADMIN.name())
                .antMatchers(POST,"/users/user-info").hasAuthority(ADMIN.name())
                .antMatchers("/cart").hasAuthority(USER.name())
                .antMatchers("/cart").hasAuthority(ADMIN.name())
                .antMatchers("/product").hasAuthority(ADMIN.name())
                .antMatchers("/product/update-product").hasAuthority(ADMIN.name())
                .antMatchers("/product/delete-product").hasAuthority(ADMIN.name())
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
