package com.SprnigSecurity.jwtAuth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService implements InitializingBean {
    private final RedisTemplate redisTemplate;

    static private final String refreshTokenNamespace = "SECURITY_JWT_REFRESH_TOKEN:";

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


    // BLACK_LIST(token & refresh token)
}
