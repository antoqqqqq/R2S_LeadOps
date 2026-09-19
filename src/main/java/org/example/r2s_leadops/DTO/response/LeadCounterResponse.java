package org.example.r2s_leadops.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadCounterResponse {
    private Long totalLeads;
    @JsonProperty("new")
    private Long newLeads;
    private Long nurtureLeads;
    private Long warmLeads;
    private Long hotLeads;
    private Long saleLeads;
    private Long lostLeads;
    private Long wonLeads;
}
