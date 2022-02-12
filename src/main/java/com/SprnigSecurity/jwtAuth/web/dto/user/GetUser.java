package com.SprnigSecurity.jwtAuth.web.dto.user;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class GetUser {
    @Getter
    @NoArgsConstructor
    public static class ResponseDto {
        private String name;
        private String email;
        private List<String> roles;

        @Builder
        public ResponseDto(String name, String email, List<String> roles) {
            this.name = name;
            this.email = email;
            this.roles = roles;
        }
    }
}
