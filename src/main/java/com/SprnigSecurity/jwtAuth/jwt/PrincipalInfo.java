package com.SprnigSecurity.jwtAuth.jwt;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class PrincipalInfo {
    private Long id;
    private String email;
    private Collection<GrantedAuthority> authorities;

    public PrincipalInfo(Long id, String email, Collection<GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
    }
}
