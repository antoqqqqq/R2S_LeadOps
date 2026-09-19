package org.example.r2s_leadops.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.r2s_leadops.constant.enumarate.LeadSourceEnum;
import org.example.r2s_leadops.constant.enumarate.LeadStage;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadResponse {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String manychatId;
    private String messengerId;
    private String zaloUid;
    private LeadSourceEnum leadSource;
    private String campaignCode;
    private String currentLevel;
    private String careerGoal;
    private String painPoint;
    private String startTimeline;
    private String preferredChannel;
    private Integer fitScore;
    private Integer engagementScore;
    private Integer intentScore;
    private Integer totalScore;
    private LeadStage leadStage;
    private Integer ownerId;
    private String ownerName;
    private Instant lastActivityAt;
    private Instant nextActionAt;
    private Boolean doNotContact;
    private Instant createdAt;
    private Instant updatedAt;
}
