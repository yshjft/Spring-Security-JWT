package com.SprnigSecurity.jwtAuth.web.api;

import com.SprnigSecurity.jwtAuth.service.redis.RedisService;
import com.SprnigSecurity.jwtAuth.service.auth.AuthService;
import com.SprnigSecurity.jwtAuth.web.dto.auth.RefreshToken;
import com.SprnigSecurity.jwtAuth.web.dto.auth.RefreshToken.AccessTokenDto;
import com.SprnigSecurity.jwtAuth.web.dto.auth.SignIn;
import com.SprnigSecurity.jwtAuth.web.dto.auth.SignIn.ResponseDto;
import com.SprnigSecurity.jwtAuth.web.dto.auth.SignIn.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.SprnigSecurity.jwtAuth.web.dto.ResponseMap.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final RedisService redisService;

    @Value("${jwt.refresh-token}") private String refreshToken;
    @Value("${jwt.refresh-token-valid-second}") private long refreshTokenValidityInMs;

    @PostMapping("/signIn")
    public ResponseEntity<Map> signIn(@RequestBody @Validated SignIn.RequestDto requestDto) {
        TokenDto tokenDto = authService.signIn(requestDto);
        redisService.setRefreshToken(requestDto.getEmail(), tokenDto.getRefreshToken(), refreshTokenValidityInMs);

        Map<String, Object> responseMap = creatResponseMap(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        responseMap.put("result", new ResponseDto(tokenDto.getAccessToken()));

        // 배포시 secure, domain 설정해야함
        ResponseCookie responseCookie = ResponseCookie.from(refreshToken, tokenDto.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenValidityInMs)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(responseMap);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Map> refreshToken() {
        AccessTokenDto accessTokenDto= authService.refreshAccessToken();

        Map<String, Object> responseMap = creatResponseMap(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        responseMap.put("result", new RefreshToken.ResponseDto(accessTokenDto.getAccessToken()));

        return ResponseEntity.ok()
                .body(responseMap);
    }

    @PostMapping("/signOut")
    public ResponseEntity<Map> signOut() {
        authService.signOut();

        Map<String, Object> responseMap = creatResponseMap(HttpStatus.OK.value(), "logout success");

        ResponseCookie responseCookie = ResponseCookie.from(refreshToken, null)
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(responseMap);
    }
}
