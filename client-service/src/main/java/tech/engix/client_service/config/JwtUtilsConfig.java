package tech.engix.client_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.engix.jwtutils.service.JwtUtils;

@Configuration
public class JwtUtilsConfig {

    @Value(value = "${tech.engix.jwtSecret}")
    private String secret;

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(secret);
    }
}
