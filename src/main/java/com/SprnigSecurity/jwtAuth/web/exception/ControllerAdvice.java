package com.SprnigSecurity.jwtAuth.web.exception;

import com.SprnigSecurity.jwtAuth.web.exception.customException.DuplicateUserException;
import com.SprnigSecurity.jwtAuth.web.exception.customException.LoginFailException;
import com.SprnigSecurity.jwtAuth.web.exception.customException.UserNotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    private Map<String, Object> createResponseMap(int status, String message, String code ) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", status);
        responseMap.put("message", message);
        responseMap.put("code", code);
        return responseMap;
    }

    @Getter
    @NoArgsConstructor
    private static class Error {
        private String detail;
        public Error(String detail) {
            this.detail = detail;
        }
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<Map> duplicateUserExceptionHandler(DuplicateUserException e) {
        Map responseMap = createResponseMap(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "40001");
        responseMap.put("error", new Error("duplicate user"));
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map> userNotFoundException(UserNotFoundException e) {
        Map responseMap = createResponseMap(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), "40401");
        responseMap.put("error", new Error("user not found"));
        return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginFailException.class)
    public ResponseEntity<Map> loginFailExceptionHandler(LoginFailException e) {
        Map responseMap = createResponseMap(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "40002");
        responseMap.put("error", new Error("login fail"));
        return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
    }
}
