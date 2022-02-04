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
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
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
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManagerBean(), jwtConfig);
        authenticationFilter.setFilterProcessesUrl("/users/api/login"); // path for normal user login
        authenticationFilter.setFilterProcessesUrl("/users/store-manager/login"); // path for store manager login
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(authenticationFilter)
                .addFilterBefore(new AuthorizationCustomFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/", "/api/drive").permitAll()
                .antMatchers(GET, "/product/api/**").permitAll()
                .antMatchers(POST, "/users/api/register", "/users/api/login", "/users/store-manager/login").permitAll()
                .antMatchers(GET,"/users/user-info").hasAnyAuthority(USER.name(), ADMIN.name())
                .antMatchers(POST,"/users/user-info").hasAnyAuthority(USER.name(), ADMIN.name())
                .antMatchers(GET,"/users/user-discount").hasAnyAuthority(USER.name(), ADMIN.name())
                .antMatchers("/users/**").hasAuthority(ADMIN.name())
                .antMatchers(POST,"/cart/add-cart").hasAnyAuthority(USER.name(), ADMIN.name())
                .antMatchers(GET,"/cart/cart-info").hasAnyAuthority(USER.name(), ADMIN.name())
                .antMatchers("/cart/**").hasAuthority(ADMIN.name())
                .antMatchers("/product/**").hasAuthority(ADMIN.name())
                .antMatchers("/discount/discount-info").hasAnyAuthority(USER.name(), ADMIN.name())
                .antMatchers("/discount/**").hasAuthority(ADMIN.name())
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
