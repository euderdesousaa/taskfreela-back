package tech.engix.api_gateway.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpCookie;
import org.springframework.util.SerializationUtils;
import org.springframework.web.server.ServerWebExchange;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Optional;

@RequiredArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class CookieUtils {

    public static Optional<String> getCookie(ServerWebExchange exchange, String name) {
        return Optional.ofNullable(exchange.getRequest()
                        .getCookies()
                        .getFirst(name))
                .map(HttpCookie::getValue);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object) {
        return Base64
                .getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decodedBytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return cls.cast(objectInputStream.readObject());
        } catch (Exception e) {
            throw new RuntimeException("Deserialization error", e);
        }
    }
}
