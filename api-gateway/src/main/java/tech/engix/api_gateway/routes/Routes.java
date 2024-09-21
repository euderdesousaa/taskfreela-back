package tech.engix.api_gateway.routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Routes {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authModule", r -> r.path("/api/v1/auth/**",
                                "/api/v1/accounts/**", "/oauth2/authorize/**")
                        .uri("lb://auth-service")
                )
                .route("taskModule", r -> r.path("/api/v1/tasks/**")
                        .uri("lb://tasks-service")
                )
                .build();
    }


}
