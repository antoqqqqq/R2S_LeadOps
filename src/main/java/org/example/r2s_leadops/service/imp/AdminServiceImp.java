package org.example.r2s_leadops.service.imp;

import org.example.r2s_leadops.DTO.PageResponse;
import org.example.r2s_leadops.DTO.request.AdminResetPasswordRequest;
import org.example.r2s_leadops.DTO.response.LeadResponse;
import org.example.r2s_leadops.DTO.response.UserResponse;
import org.example.r2s_leadops.constant.enumarate.LeadSourceEnum;
import org.example.r2s_leadops.constant.enumarate.LeadStage;
import org.example.r2s_leadops.constant.enumarate.UserRole;
import org.example.r2s_leadops.entity.User;
import org.example.r2s_leadops.entity.UserStatus;
import org.example.r2s_leadops.exception.ResourceNotFoundException;
import org.example.r2s_leadops.mapper.LeadMapper;
import org.example.r2s_leadops.mapper.UserMapper;
import org.example.r2s_leadops.repository.LeadOpportunityRepository;
import org.example.r2s_leadops.repository.UserRepository;
import org.example.r2s_leadops.repository.spec.LeadOpportunitySpecifications;
import org.example.r2s_leadops.service.AdminService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImp implements AdminService {

    private final UserRepository userRepository;
    private final LeadOpportunityRepository leadOpportunityRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImp(
            UserRepository userRepository,
            LeadOpportunityRepository leadOpportunityRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.leadOpportunityRepository = leadOpportunityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageResponse<List<UserResponse>> getUsers(
            int page, int size, UserRole role, UserStatus status, String search) {

        Specification<User> spec = Specification.where(hasRole(role))
                .and(hasStatus(status))
                .and(matchesSearch(search));

        var userPage = userRepository.findAll(spec, PageRequest.of(page, size));
        return PageResponse.from(userPage, UserMapper::toResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {
        return UserMapper.toResponse(findUserOrThrow(id));
    }

    @Override
    @Transactional
    public UserResponse lockUser(Long userId) {
        User user = findUserOrThrow(userId);
        user.setStatus(UserStatus.LOCKED);
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse unlockUser(Long userId) {
        User user = findUserOrThrow(userId);
        user.setStatus(UserStatus.ACTIVE);
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Override
    public PageResponse<List<LeadResponse>> getUserLeads(
            Long userId, int page, int size, LeadStage stage, LeadSourceEnum source, String search) {
        findUserOrThrow(userId);

        var spec = Specification
                .where(LeadOpportunitySpecifications.hasOwner(userId))
                .and(LeadOpportunitySpecifications.hasStage(stage))
                .and(LeadOpportunitySpecifications.hasSource(source))
                .and(LeadOpportunitySpecifications.searchByPerson(search));

        var result = leadOpportunityRepository.findAll(
                spec, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt")));

        return PageResponse.from(result, LeadMapper::toResponse);
    }

    @Override
    @Transactional
    public void resetPassword(Long userId, AdminResetPasswordRequest request) {
        User user = findUserOrThrow(userId);
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> ResourceNotFoundException.userNotFound(userId));
    }

    private Specification<User> hasRole(UserRole role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("role").get("code"), role);
    }

    private Specification<User> hasStatus(UserStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    private Specification<User> matchesSearch(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return null;
            }
            String pattern = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("fullName")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
            );
        };
    }
}
