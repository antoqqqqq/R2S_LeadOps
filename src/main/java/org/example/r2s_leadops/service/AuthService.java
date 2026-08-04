package org.example.r2s_leadops.service;

import java.util.Locale;

import org.example.r2s_leadops.entity.UserStatus;
import org.example.r2s_leadops.DTO.request.AuthResponse;
import org.example.r2s_leadops.DTO.response.LoginRequest;
import org.example.r2s_leadops.DTO.response.RegisterRequest;
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
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
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

        Role defaultRole = roleRepository.findByName("ROLE_SALES")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_SALES");
                    role.setDescription("Nhân viên tư vấn (Sales)");
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
}
