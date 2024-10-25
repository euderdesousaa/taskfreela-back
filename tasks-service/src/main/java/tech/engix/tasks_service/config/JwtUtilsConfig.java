package tech.engix.tasks_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.engix.jwtutils.service.JwtUtilsService;

@Configuration
public class JwtUtilsConfig {

    @Value(value = "${tech.engix.jwtSecret}")
    private String secret;

    @Bean
    public JwtUtilsService jwtUtils() {
        return new JwtUtilsService(secret);
    }
}
