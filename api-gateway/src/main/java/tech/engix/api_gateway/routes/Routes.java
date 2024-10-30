package tech.engix.api_gateway.routes;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Routes {
    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("authModule", r -> r.path("/api/v1/auth/**",
                        "/api/v1/accounts/**", "/oauth2/authorize/**",
                        "/api/v1/recovery/**", "/auth-service/v3/api-docs", "/api/v1/refresh/**")
                        .uri("lb://auth-service")
                )
                .route("tasksModule", r -> r.path("/api/v1/tasks/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://tasks-service")
                )
                .route("projectModule", r -> r.path("/api/v1/project/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://project-service")
                )
                .route("clientModule", r -> r.path("/api/v1/client/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://client-service")
                )
                .route("sendEmailModule", r -> r.path("/api/v1/email/**")
                        .uri("lb://send-email-service")
                )
                .build();
    }

}
