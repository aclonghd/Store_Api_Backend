package com.java.store;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt.properties")
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class JWTConfig {
    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfterDays;
}
