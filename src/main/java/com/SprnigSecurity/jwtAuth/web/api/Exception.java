package com.SprnigSecurity.jwtAuth.web.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/exception")
public class Exception {
    // 토큰X

    // invalid token

    // invalid refresh token

    // black list

    // 인증
    @GetMapping("/unauthenticated")
    public void unAuthenticatedException() {

    }

    // 권한
    @GetMapping("/accessDenied")
    public void accessDeniedException() {

    }
}
