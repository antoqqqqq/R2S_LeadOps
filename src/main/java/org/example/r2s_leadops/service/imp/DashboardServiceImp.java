package org.example.r2s_leadops.service.imp;

import lombok.RequiredArgsConstructor;
import org.example.r2s_leadops.DTO.response.LeadCounterResponse;
import org.example.r2s_leadops.DTO.response.LeadResourceResponse;
import org.example.r2s_leadops.DTO.response.TopLeadsResponse;
import org.example.r2s_leadops.repository.DashboardRepository;
import org.example.r2s_leadops.repository.LeadOpportunityRepository;
import org.example.r2s_leadops.service.DashboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImp implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    public LeadCounterResponse getLeadCounter() {
        return dashboardRepository.getLeadCounter();
    }

    @Override
    public List<LeadResourceResponse> getLeadResource() {
        return dashboardRepository.getLeadResource();
    }

    @Override
    public List<TopLeadsResponse> getTopLeads() {
        return dashboardRepository.getTopLeads(PageRequest.of(0, 5));
    }


}
