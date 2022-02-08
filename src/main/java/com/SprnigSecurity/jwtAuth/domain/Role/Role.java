package com.SprnigSecurity.jwtAuth.domain.Role;

import com.SprnigSecurity.jwtAuth.domain.BaseEntity;
import com.SprnigSecurity.jwtAuth.domain.userRole.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Role{
    @Id
    private String role;

    @OneToMany(mappedBy = "role")
    List<UserRole> userRoles = new ArrayList<>();

    public Role(String role) {
        this.role = role;
    }
}
