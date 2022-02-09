package com.SprnigSecurity.jwtAuth.domain.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, String> {
    @Query("select r from Role r where r.role in (:roles)")
    List<Role> findRoles(@Param("roles")List<String> roles);
}
