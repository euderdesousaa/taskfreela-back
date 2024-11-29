package tech.engix.api_gateway.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.web.server.ServerWebExchange;

import java.util.Optional;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class CookieUtils {

    public static Optional<String> getCookie(ServerWebExchange exchange, String name) {
        return Optional.ofNullable(exchange.getRequest()
                        .getCookies()
                        .getFirst(name))
                .map(HttpCookie::getValue);
    }

}
