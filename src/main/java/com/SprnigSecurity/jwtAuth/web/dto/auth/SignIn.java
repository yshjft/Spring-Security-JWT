package com.SprnigSecurity.jwtAuth.web.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignIn {
    @Getter
    @NoArgsConstructor
    public static class RequestDto {
        private String email;
        private String password;

        @Builder
        public RequestDto(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @Getter
    public static class TokenDto {
        private String accessToken;
        private String refreshToken;

        @Builder
        public TokenDto(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ResponseDto { // 헤더에 반드시 refreshToken이 있어야 한다.
        private String accessToken;

        public ResponseDto(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
