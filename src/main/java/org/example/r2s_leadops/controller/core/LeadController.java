package org.example.r2s_leadops.controller.core;

import lombok.RequiredArgsConstructor;
import org.example.r2s_leadops.DTO.PageResponse;
import org.example.r2s_leadops.DTO.response.LeadResponse;
import org.example.r2s_leadops.entity.Lead;
import org.example.r2s_leadops.service.LeadService;
import org.example.r2s_leadops.service.imp.LeadServiceImp;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import org.example.r2s_leadops.dto.request.LeadUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
public class LeadController {
    private final LeadServiceImp leadService;

    @GetMapping
    public ResponseEntity<PageResponse<List<LeadResponse>>> getLeads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {

        return ResponseEntity.ok(leadService.getLeads(page, size, search)); // Placeholder for actual implementation
    }

    @GetMapping("/me")
    public ResponseEntity<PageResponse<List<LeadResponse>>> getMyLeads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search) {

        return ResponseEntity.ok(leadService.getmyLeads(page, size, search)); // Placeholder for actual implementation
    }

    @PostMapping
    public ResponseEntity<LeadResponse> createLead(@RequestBody Lead lead) {
        // Implementation for creating a new lead
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadResponse> updateLead(@PathVariable Long id, @RequestBody LeadUpdateRequest request) {

        return ResponseEntity.ok().build();
    }
}
