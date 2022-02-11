package com.SprnigSecurity.jwtAuth.service.redis;

import com.SprnigSecurity.jwtAuth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService implements InitializingBean {
    private final RedisTemplate redisTemplate;
    private final TokenProvider tokenProvider;

    @Value("${Spring.redis.refresh-token-namespace}") private String refreshTokenNamespace;
    @Value("${Spring.redis.black-list-namespace}") private String blackListNamespace;
    private ValueOperations<String, String> valueOperations;

    @Override
    public void afterPropertiesSet() throws Exception {
        valueOperations = redisTemplate.opsForValue();
    }

    // get refresh token
    public String getRefreshToken(String userEmail) { // key 이메일
        String key = refreshTokenNamespace + userEmail;
        return valueOperations.get(key);
    }

    // set refresh token
    public void setRefreshToken(String userEmail, String refreshToken, long duration) {
        String key = refreshTokenNamespace + userEmail;
        Duration expiredDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, refreshToken, expiredDuration);
    }

    // delete refresh token
    public void deleteRefreshToken(String userEmail) {
        String key = refreshTokenNamespace + userEmail;

        if(valueOperations.get(key) != null) {
            redisTemplate.delete(key);
        }
    }

    // set black list
    public void setBlackList(String accessToken) {
        String key = blackListNamespace + accessToken;

        log.info("expire {}", tokenProvider.getExpiration(accessToken));

        Duration expiredDuration = Duration.ofMillis(tokenProvider.getExpiration(accessToken));
        valueOperations.set(key, "INVALID_ACCESS_TOKEN", expiredDuration);
    }
}
