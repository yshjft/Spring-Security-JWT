package com.SprnigSecurity.jwtAuth.web.dto.user;

import com.SprnigSecurity.jwtAuth.domain.User.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class SignUp {
    @Getter
    @NoArgsConstructor
    public static class RequestDto {
        @NotBlank
        @Length(max=30)
        private String name;

        @NotBlank
        @Email
        @Length(max = 50)
        private String email;

        @NotBlank
        @Length(min = 4, max = 10) // 상세 validation 생략
        private String password;

        private String encodedPassword;

        @NotNull
        @Size(min = 1, max = 2) // 상세 validation 생략
        private List<String> roles;

        @Builder
        public RequestDto(String name, String email, String password, String encodedPassword, List<String> roles) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.encodedPassword = encodedPassword;
            this.roles = roles;
        }

        public void setEncodedPassword(String encodedPassword) {
            this.encodedPassword = encodedPassword;
        }

        public User toEntity() {
            return User.builder()
                    .name(name)
                    .email(email)
                    .password(encodedPassword)
                    .build();
        }
    }

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
