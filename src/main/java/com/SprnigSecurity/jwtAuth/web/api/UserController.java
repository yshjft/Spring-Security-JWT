package com.SprnigSecurity.jwtAuth.web.api;

import com.SprnigSecurity.jwtAuth.service.UserService;
import com.SprnigSecurity.jwtAuth.web.dto.user.SignUp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    private Map<String, Object> creatResponseMap(int status, String message) {
        Map<String, Object> responseMap = new LinkedHashMap<>();
        responseMap.put("status", status);
        responseMap.put("message", message);
        return responseMap;
    }

    @PostMapping("/signUp")
    public ResponseEntity<Map> signUp(@RequestBody @Validated SignUp.RequestDto requestDto) {
        SignUp.ResponseDto responseDto = userService.signUpService(requestDto);

        Map<String, Object> responseMap = creatResponseMap(HttpStatus.CREATED.value(), "sign up success");
        responseMap.put("result", responseDto);

        return new ResponseEntity(responseMap, HttpStatus.CREATED);
    }
}
