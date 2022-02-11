package com.SprnigSecurity.jwtAuth.web.api;

import com.SprnigSecurity.jwtAuth.web.exception.customException.ExpiredTokenException;
import com.SprnigSecurity.jwtAuth.web.exception.customException.UnauthorizedException;
import com.SprnigSecurity.jwtAuth.web.exception.customException.WrongTokenException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/exception")
public class ExceptionController {
    // black list

    // 인증
    @GetMapping("/unauthorized")
    public void unAuthorizedException() {
        throw new UnauthorizedException();
    }

    // 권한
    @GetMapping("/accessDenied")
    public void accessDeniedException() {

    }

    // expired token
    @GetMapping("/expiredToken")
    public void expiredTokenException() {
        throw new ExpiredTokenException();
    }

    // wrong token
    @GetMapping("/wrongToken")
    public void wrongToken() {
        throw new WrongTokenException();
    }
}
