package org.example.r2s_leadops.service;

import org.example.r2s_leadops.DTO.response.LeadCounterResponse;
import org.example.r2s_leadops.DTO.response.LeadResourceResponse;
import org.example.r2s_leadops.DTO.response.TopLeadsResponse;

import java.util.List;

public interface DashboardService {
    LeadCounterResponse getLeadCounter();
    List<LeadResourceResponse> getLeadResource();
    List<TopLeadsResponse> getTopLeads();
}
