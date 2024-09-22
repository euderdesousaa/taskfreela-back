package tech.engix.auth_service.security.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value(value = "${tech.engix.jwtRefreshToken}")
    private Long jwtTokenTime;

    public void saveRefreshToken(String username, String refreshToken) {
        Duration expirationDuration = Duration.ofMillis(jwtTokenTime);
        redisTemplate.opsForValue().set(username, refreshToken, expirationDuration);
    }

    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }
}
