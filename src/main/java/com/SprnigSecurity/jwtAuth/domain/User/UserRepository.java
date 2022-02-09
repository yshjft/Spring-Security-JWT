package com.SprnigSecurity.jwtAuth.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("select u from User u join fetch u.userRoles where u.email = :email")
    Optional<User> findUserWithRolesByEmail(@Param("email")String email);
}
