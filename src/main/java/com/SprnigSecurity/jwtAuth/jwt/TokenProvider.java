package com.SprnigSecurity.jwtAuth.jwt;

import com.SprnigSecurity.jwtAuth.service.auth.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {
    private static final String AUTHORITIES_KEY = "AUTHORITIES";
    private static final String USER_EMAIL_KEY = "USER_EMAIL";

    private final String secret;
    private final long tokenValidityInMs;
    private final long refreshTokenValidityInMs;

    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-valid-second}") long tokenValidityInMs,
            @Value("${jwt.refresh-token-valid-second}") long refreshTokenValidityInMs) {
        this.secret = secret;
        this.tokenValidityInMs = tokenValidityInMs * 1000;
        this.refreshTokenValidityInMs = refreshTokenValidityInMs * 1000;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Authentication authentication) {
        String userEmail = authentication.getName();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expiredTime = new Date(now.getTime() + this.tokenValidityInMs);

        return Jwts.builder()
                // header
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // token type
                .setSubject("ACCESS_TOKEN") // 토큰의 제목
                // payload
                .claim(USER_EMAIL_KEY, userEmail)
                .claim(AUTHORITIES_KEY, authorities)
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                // signature
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken() {

        Date now = new Date();
        Date expiredTime = new Date(now.getTime() + this.refreshTokenValidityInMs);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject("REFRESH_TOKEN")
                .setIssuedAt(now)
                .setExpiration(expiredTime)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userEmail = (String)claims.get(USER_EMAIL_KEY);
        Collection<? extends  GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(userEmail, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}