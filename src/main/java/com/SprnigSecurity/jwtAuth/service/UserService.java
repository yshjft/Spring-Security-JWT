package com.SprnigSecurity.jwtAuth.service;

import com.SprnigSecurity.jwtAuth.domain.Role.Role;
import com.SprnigSecurity.jwtAuth.domain.Role.RoleRepository;
import com.SprnigSecurity.jwtAuth.domain.User.User;
import com.SprnigSecurity.jwtAuth.domain.User.UserRepository;
import com.SprnigSecurity.jwtAuth.domain.userRole.UserRole;
import com.SprnigSecurity.jwtAuth.web.dto.user.SignUp;
import com.SprnigSecurity.jwtAuth.web.exception.customException.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public SignUp.ResponseDto signUpService(SignUp.RequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateUserException();
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        requestDto.setEncodedPassword(encodedPassword);

        User user = requestDto.toEntity();
        List<Role> roles = roleRepository.findRoles(requestDto.getRoles());

        List<UserRole> userRoles = new ArrayList<>();
        for(Role role : roles) {
            userRoles.add(new UserRole(user, role));
        }

        userRepository.save(user);

        List<String> savedRole = new ArrayList<>();
        for(UserRole userRole : userRoles) {
            savedRole.add(userRole.getRole().getRole());
        }

        return SignUp.ResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .roles(savedRole)
                .build();
    }
}
