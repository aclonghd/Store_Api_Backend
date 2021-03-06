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
import org.springframework.security.web.header.writers.StaticHeadersWriter;

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
        AuthenticationFilter authenticationManagerFilter = new AuthenticationFilter(authenticationManagerBean(), jwtConfig, "manager");
        AuthenticationFilter authenticationUserFilter = new AuthenticationFilter(authenticationManagerBean(), jwtConfig, "user");
        authenticationUserFilter.setFilterProcessesUrl("/users/api/login"); // path for normal user login
        authenticationManagerFilter.setFilterProcessesUrl("/users/store-manager/login"); // path for store manager login
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(authenticationManagerFilter)
                .addFilter(authenticationUserFilter)
                .addFilterBefore(new AuthorizationCustomFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/", "/api/drive").permitAll()
                .antMatchers(GET, "/products/api/**", "/review/api/**", "/carts/api/**").permitAll()
                .antMatchers(POST, "/reviews/api/**", "/carts/api/**").permitAll()
                .antMatchers(POST, "/users/api/register", "/users/api/login", "/users/store-manager/login").permitAll()
                .antMatchers(GET,"/users/user-info").hasAnyRole(USER.name(), ADMIN.name())
                .antMatchers(POST,"/users/user-info").hasAnyRole(USER.name(), ADMIN.name())
                .antMatchers(GET,"/users/user-discount").hasAnyRole(USER.name(), ADMIN.name())
                .antMatchers("/users/**").hasRole(ADMIN.name())
                .antMatchers("/carts/**").hasRole(ADMIN.name())
                .antMatchers("/products/**").hasRole(ADMIN.name())
                .antMatchers("/discounts/discount-info").hasAnyRole(USER.name(), ADMIN.name())
                .antMatchers("/discounts/**").hasRole(ADMIN.name())
                .antMatchers("/reviews/**").hasRole(ADMIN.name())
                .anyRequest()
                .authenticated()
                .and().headers()
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin","*"))
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers","Authorization"))
                .addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Methods", "DELETE, POST, GET, OPTIONS"))
                .and().cors();
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
