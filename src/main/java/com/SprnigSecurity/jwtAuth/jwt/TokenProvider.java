package com.SprnigSecurity.jwtAuth.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {
    private final String secret;
    private final long tokenValidityInMs;
    private final long refreshTokenValidityInMs;

    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-valid-second}") long tokenValidityInMs,
            @Value("${jwt.refresh-token-valid-second}") long refreshTokenValidityInMs) {
        this.secret = secret;
        this.tokenValidityInMs = tokenValidityInMs;
        this.refreshTokenValidityInMs = refreshTokenValidityInMs;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        authentication.getPrincipal(); // id, 이메일, 권한 저장
        return "";
    }

    public String createRefreshToken() {
        return "";
    }

    public boolean validateToken() {
        return true;
    }
}