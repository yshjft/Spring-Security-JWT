package com.SprnigSecurity.jwtAuth.domain.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Role{
    @Id
    private String role;

    public Role(String role) {
        this.role = role;
    }
}
