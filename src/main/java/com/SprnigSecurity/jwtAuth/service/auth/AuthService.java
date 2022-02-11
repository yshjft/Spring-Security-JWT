package com.SprnigSecurity.jwtAuth.service.auth;

import com.SprnigSecurity.jwtAuth.jwt.TokenProvider;
import com.SprnigSecurity.jwtAuth.service.redis.RedisService;
import com.SprnigSecurity.jwtAuth.web.dto.auth.RefreshToken.AccessTokenDto;
import com.SprnigSecurity.jwtAuth.web.dto.auth.SignIn.TokenDto;
import com.SprnigSecurity.jwtAuth.web.exception.customException.InvalidRefreshTokenException;
import com.SprnigSecurity.jwtAuth.web.exception.customException.LoginFailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;

import static com.SprnigSecurity.jwtAuth.web.dto.auth.SignIn.*;
import static org.springframework.util.StringUtils.hasText;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService{
    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${jwt.refresh-token}") private String REFRESH_TOKEN_HEADER;

    public TokenDto signIn(RequestDto requestDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken();

            return TokenDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        }catch (Exception e) {
            throw new LoginFailException();
        }
    }

    public AccessTokenDto refreshAccessToken() {
        String refreshToken = resolveRefreshToken();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        String savedRefreshToken = redisService.getRefreshToken(userEmail);

        if(!hasText(refreshToken) || !hasText(savedRefreshToken) || !refreshToken.equals(savedRefreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        String accessToken = tokenProvider.createAccessToken(authentication);
        return new AccessTokenDto(accessToken);
    }

    public void signOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        String accessToken = (String)authentication.getCredentials();

        redisService.setBlackList(accessToken);
        redisService.deleteRefreshToken(userEmail);
    }

    private String resolveRefreshToken() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        Cookie[] cookies = servletRequestAttributes.getRequest().getCookies();
        String refreshToken = null;

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(REFRESH_TOKEN_HEADER)){
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        return refreshToken;
    }
}
