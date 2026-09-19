package org.example.r2s_leadops.service;

import org.example.r2s_leadops.DTO.PageResponse;
import org.example.r2s_leadops.DTO.response.LeadResponse;
import org.springframework.data.domain.Page;
import org.example.r2s_leadops.dto.request.LeadUpdateRequest;

import java.util.List;

public interface LeadService {
    PageResponse<List<LeadResponse>> getmyLeads(int page, int size, String search);
    PageResponse<List<LeadResponse>> getLeads(int page, int size, String search);
    Boolean updateLead(Long id, LeadUpdateRequest request);
}
