package tech.engix.api_gateway.routes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class RouterValidator {
    public static final List<String> openEndPoint = List.of(
            "/api/v1/auth/**"
    );

    private static final Predicate<ServerHttpRequest> IS_SECURED =
            serverHttpRequest -> openEndPoint.stream().noneMatch(uri -> serverHttpRequest.getURI().getPath().contains(uri));

    private static Predicate<ServerHttpRequest> getIsSecuredPredicate() {
        return IS_SECURED;
    }

    Predicate<ServerHttpRequest> isSecured = RouterValidator.getIsSecuredPredicate();

}
