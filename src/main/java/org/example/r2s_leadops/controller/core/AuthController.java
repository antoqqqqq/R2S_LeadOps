package org.example.r2s_leadops.controller.core;

import jakarta.validation.Valid;
import org.example.r2s_leadops.DTO.ApiResponse;
import org.example.r2s_leadops.DTO.request.ChangePasswordRequest;
import org.example.r2s_leadops.DTO.request.ForgotPasswordRequest;
import org.example.r2s_leadops.DTO.response.AuthResponse;
import org.example.r2s_leadops.DTO.response.UserResponse;
import org.example.r2s_leadops.DTO.request.LoginRequest;
import org.example.r2s_leadops.DTO.request.RegisterRequest;
import org.example.r2s_leadops.service.imp.AuthServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImp authService;

    public AuthController(AuthServiceImp authService) {
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

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Logout successful",
                        null
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "User information retrieved successfully",
                        authService.getCurrentUser(userDetails.getUsername())
                )
        );
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        authService.forgotPassword(request);

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Password reset link has been sent",
                        null
                )
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        authService.changePassword(
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Password changed successfully",
                        null
                )
        );
    }
}
