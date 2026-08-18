package org.example.r2s_leadops.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class UserLeadsResponse {

    private String id;
    private String fullName;
    private String phone;
    private String email;

    private String manychatId;
    private String messengerId;
    private String zaloUid;

    private String leadSource;
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

    private String leadStage;

    private Long ownerId;
    private String ownerName;

    private OffsetDateTime lastActivityAt;
    private OffsetDateTime nextActionAt;

    private Boolean doNotContact;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}