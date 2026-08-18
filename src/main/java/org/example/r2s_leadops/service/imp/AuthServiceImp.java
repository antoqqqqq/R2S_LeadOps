package org.example.r2s_leadops.service.imp;

import java.util.Locale;

import jakarta.validation.Valid;
import org.example.r2s_leadops.DTO.request.ChangePasswordRequest;
import org.example.r2s_leadops.DTO.request.ForgotPasswordRequest;
import org.example.r2s_leadops.constant.enumarate.UserRole;
import org.example.r2s_leadops.DTO.response.UserResponse;
import org.example.r2s_leadops.entity.UserStatus;
import org.example.r2s_leadops.mapper.UserMapper;
import org.example.r2s_leadops.DTO.response.AuthResponse;
import org.example.r2s_leadops.DTO.request.LoginRequest;
import org.example.r2s_leadops.DTO.request.RegisterRequest;
import org.example.r2s_leadops.entity.Role;
import org.example.r2s_leadops.entity.User;
import org.example.r2s_leadops.repository.RoleRepository;
import org.example.r2s_leadops.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtServiceImp jwtService;

    public AuthServiceImp(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtServiceImp jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role defaultRole = roleRepository.findByCode(UserRole.STAFF)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setCode(UserRole.STAFF);
                    role.setDescription("Nhân viên tư vấn (Sales/Marketing)");
                    return roleRepository.save(role);
                });

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail().trim().toLowerCase(Locale.ROOT));
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(defaultRole);
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateAccessToken(savedUser);

        return new AuthResponse(token, "Bearer");
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().trim().toLowerCase(Locale.ROOT),
                        request.getPassword()));

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtService.generateAccessToken(user);
        return new AuthResponse(token, "Bearer");
    }

    public void changePassword(String username, @Valid ChangePasswordRequest request) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void forgotPassword(@Valid ForgotPasswordRequest request) {
    }

    public UserResponse getCurrentUser(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserMapper.toResponse(user);
    }
}
