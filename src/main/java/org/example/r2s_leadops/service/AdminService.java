package org.example.r2s_leadops.service;

import org.example.r2s_leadops.DTO.PageResponse;
import org.example.r2s_leadops.DTO.request.AdminResetPasswordRequest;
import org.example.r2s_leadops.DTO.response.LeadResponse;
import org.example.r2s_leadops.DTO.response.UserResponse;
import org.example.r2s_leadops.constant.enumarate.LeadSourceEnum;
import org.example.r2s_leadops.constant.enumarate.LeadStage;
import org.example.r2s_leadops.constant.enumarate.UserRole;
import org.example.r2s_leadops.entity.UserStatus;

import java.util.List;

public interface AdminService {
    PageResponse<List<UserResponse>> getUsers(
            int page, int size, UserRole role, UserStatus status, String search);

    UserResponse getUserById(Long userId);

    UserResponse lockUser(Long userId);

    UserResponse unlockUser(Long userId);

    PageResponse<List<LeadResponse>> getUserLeads(
            Long userId, int page, int size, LeadStage stage, LeadSourceEnum source, String search);

    void resetPassword(Long userId, AdminResetPasswordRequest request);
}
