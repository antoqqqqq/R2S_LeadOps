package org.example.r2s_leadops.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLeadRequest {

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

    private Boolean doNotContact;
}
