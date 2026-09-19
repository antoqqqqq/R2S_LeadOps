package org.example.r2s_leadops.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopLeadsResponse {
    private Integer leadId;
    private String leadName;
    private Integer totalScore;
    private String leadStatus;
    private Integer ownerId;
    private String ownerName;
    private String courseName;
}
