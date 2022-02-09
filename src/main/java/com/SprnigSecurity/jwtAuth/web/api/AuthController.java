package com.SprnigSecurity.jwtAuth.web.api;

import com.SprnigSecurity.jwtAuth.service.auth.AuthService;
import com.SprnigSecurity.jwtAuth.web.dto.auth.SignIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    private Map<String, Object> creatResponseMap(int status, String message) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", status);
        responseMap.put("message", message);
        return responseMap;
    }

    @PostMapping("/signIn")
    public ResponseEntity<Map> signIn(@RequestBody @Validated SignIn.RequestDto requestDto) {
        SignIn.TokenDto tokenDto = authService.signIn(requestDto);

        Map<String, Object> responseMap = creatResponseMap(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        responseMap.put("result", new SignIn.ResponseDto(tokenDto.getAccessToken()));

        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(604800)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(responseMap);
    }

    // 리프레시

    // 로그아웃
}
