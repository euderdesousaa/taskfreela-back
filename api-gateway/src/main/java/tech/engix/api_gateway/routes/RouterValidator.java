package tech.engix.api_gateway.routes;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouterValidator {
    public static final List<String> openEndPoint = List.of(
            "/api/v1/auth/**"
    );

    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> openEndPoint.stream().noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));
}
