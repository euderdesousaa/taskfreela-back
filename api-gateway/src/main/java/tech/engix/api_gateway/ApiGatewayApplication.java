package tech.engix.api_gateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = @Info(title = "API Gateway", version = "1.0", description = "Documentation API Gateway v1.0"))
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/client-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://client-service"))
				.route(r -> r.path("/project-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://project-service"))
				.route(r -> r.path("/auth-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://auth-service"))
				.route(r -> r.path("/tasks-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://tasks-service"))
				.build();
	}
}
