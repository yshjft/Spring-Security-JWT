package com.SprnigSecurity.jwtAuth.web.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class RefreshToken {
    @Getter
    @NoArgsConstructor
    public static class AccessTokenDto {
        private String accessToken;
        public AccessTokenDto(String accessToken) {
            this.accessToken = accessToken;
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
