package org.example.r2s_leadops.controller.core;

import jakarta.validation.Valid;
import org.example.r2s_leadops.DTO.ApiResponse;
import org.example.r2s_leadops.DTO.request.AuthResponse;
import org.example.r2s_leadops.DTO.response.LoginRequest;
import org.example.r2s_leadops.DTO.response.RegisterRequest;
import org.example.r2s_leadops.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Registration successful",
                        authService.register(request)
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Login successful",
                        authService.login(request)
                )
        );
    }
}
