package com.SprnigSecurity.jwtAuth.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.SprnigSecurity.jwtAuth.web.dto.ResponseMap.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/withoutAuth")
    public static ResponseEntity<Map> greeting() {
        Map responseMap = creatResponseMap(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        responseMap.put("message", "this api doesn't need authentication");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/withAuth")
    public static ResponseEntity<Map> greetingWithAuth() {
        Map responseMap = creatResponseMap(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase());
        responseMap.put("message", "this api needs authentication");
        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}
