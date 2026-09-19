package org.example.r2s_leadops.controller.core;

import lombok.RequiredArgsConstructor;
import org.example.r2s_leadops.DTO.ApiResponse;
import org.example.r2s_leadops.DTO.response.LeadCounterResponse;
import org.example.r2s_leadops.DTO.response.LeadResourceResponse;
import org.example.r2s_leadops.DTO.response.TopLeadsResponse;
import org.example.r2s_leadops.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/counter-lead")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<LeadCounterResponse>> getLeadCounter() {
        LeadCounterResponse leadCounterResponse = dashboardService.getLeadCounter();
        // Set the values for the leadCounterResponse object

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Lead counter retrieved successfully",
                        leadCounterResponse
                )
        );
    }

    @GetMapping("/lead-resource")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<LeadResourceResponse>>> getLeadResource() {
        List<LeadResourceResponse> leadResourceResponses = dashboardService.getLeadResource();

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Lead resource retrieved successfully",
                        leadResourceResponses
                )
        );
    }

    @GetMapping("/top-leads")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponse<List<TopLeadsResponse>>> getTopLeads() {
        List<TopLeadsResponse> topLeadsResponses = dashboardService.getTopLeads();

        return ResponseEntity.ok(
                ResponseUtil.success(
                        "Top leads retrieved successfully",
                        topLeadsResponses
                )
        );
    }


}