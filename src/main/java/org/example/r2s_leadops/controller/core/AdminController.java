package org.example.r2s_leadops.controller.core;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.r2s_leadops.DTO.PageResponse;
import org.example.r2s_leadops.DTO.request.AdminResetPasswordRequest;
import org.example.r2s_leadops.DTO.response.LeadResponse;
import org.example.r2s_leadops.DTO.response.UserResponse;
import org.example.r2s_leadops.constant.enumarate.LeadSourceEnum;
import org.example.r2s_leadops.constant.enumarate.LeadStage;
import org.example.r2s_leadops.constant.enumarate.UserRole;
import org.example.r2s_leadops.entity.UserStatus;
import org.example.r2s_leadops.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<PageResponse<List<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) String search) {
        System.out.println("=== CONTROLLER REACHED ===");

        return ResponseEntity.ok(adminService.getUsers(page, size, role, status, search));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    @PutMapping("/{userId}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> lockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.lockUser(userId));
    }

    @PutMapping("/{userId}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> unlockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.unlockUser(userId));
    }

    @PutMapping("/{userId}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Long userId,
            @Valid @RequestBody AdminResetPasswordRequest request) {

        adminService.resetPassword(userId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{userId}/leads")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<PageResponse<List<LeadResponse>>> getUserLeads(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) LeadStage stage,
            @RequestParam(required = false) LeadSourceEnum source,
            @RequestParam(required = false) String search) {

        return ResponseEntity.ok(
                adminService.getUserLeads(userId, page, size, stage, source, search));
    }
}
