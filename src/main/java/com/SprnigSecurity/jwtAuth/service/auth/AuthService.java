package com.SprnigSecurity.jwtAuth.service.auth;

import com.SprnigSecurity.jwtAuth.jwt.TokenProvider;
import com.SprnigSecurity.jwtAuth.web.dto.auth.SignIn;
import com.SprnigSecurity.jwtAuth.web.exception.customException.LoginFailException;
import lombok.RequiredArgsConstructor;;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService{
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private ValueOperations<String, String> valueOperations;

    public SignIn.TokenDto signIn(SignIn.RequestDto requestDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenProvider.createAccessToken(authentication);
            String refreshToken = tokenProvider.createRefreshToken();

            return SignIn.TokenDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        }catch (Exception e) {
            throw new LoginFailException();
        }
    }
}
