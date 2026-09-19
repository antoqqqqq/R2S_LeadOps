package org.example.r2s_leadops.service.imp;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.r2s_leadops.DTO.PageResponse;
import org.example.r2s_leadops.DTO.response.LeadResponse;
import org.example.r2s_leadops.dto.request.LeadUpdateRequest;
import org.example.r2s_leadops.entity.User;
import org.example.r2s_leadops.mapper.LeadMapper;
import org.example.r2s_leadops.repository.LeadOpportunityRepository;
import org.example.r2s_leadops.repository.UserRepository;
import org.example.r2s_leadops.repository.spec.LeadOpportunitySpecifications;
import org.example.r2s_leadops.service.LeadService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadServiceImp implements LeadService {
    private final UserRepository userRepository;
    private final JwtServiceImp jwtService;
    private final LeadOpportunityRepository leadRepository;



    @Override
    public PageResponse<List<LeadResponse>> getmyLeads(int page, int size, String search) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Claims claims = jwtService.extractClaims(authentication);
        Long userId = Long.valueOf(jwtService.extractUserId(claims));
        var spec = Specification
                .where(LeadOpportunitySpecifications.hasOwner(userId))
                .and(LeadOpportunitySpecifications.searchByPerson(search));
        var result = leadRepository.findAll(
                spec, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt")));

        return PageResponse.from(result, LeadMapper::toResponse);
    }

    @Override
    public PageResponse<List<LeadResponse>> getLeads(int page, int size, String search) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);
        //Long userId = Long.valueOf(authentication.getName());
        if ("ROLE_ADMIN".equals(role)) {
            var spec = Specification.where(LeadOpportunitySpecifications.searchByPerson(search));

            var result = leadRepository.findAll(
                    spec, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt")));

            return PageResponse.from(result, LeadMapper::toResponse);
        }

        if ("ROLE_MANAGER".equals(role)) {
            return null;//leadRepository.findByManagerId(getCurrentUser().getId())
        }

        if ("ROLE_STAFF".equals(role)) {
            return null;//leadRepository.findByOwnerId(
            //        getCurrentUser().getId()
            //);
        }
        return null;
    }

    @Override
    public Boolean updateLead(Long id, LeadUpdateRequest request) {

        return null;
    }
}
