package org.example.r2s_leadops.mapper;

import org.example.r2s_leadops.DTO.response.LeadResponse;
import org.example.r2s_leadops.entity.LeadOpportunity;

public final class LeadMapper {

    private LeadMapper() {
    }

    public static LeadResponse toResponse(LeadOpportunity opportunity) {
        var lead = opportunity.getLead();
        var owner = opportunity.getAssignedUser();

        return LeadResponse.builder()
                .id(opportunity.getId().longValue())
                .fullName(lead.getFullName())
                .phone(lead.getPhone())
                .email(lead.getEmail())
                .manychatId(lead.getManychatId())
                .messengerId(lead.getMessengerId())
                .zaloUid(lead.getZaloUid())
                .leadSource(opportunity.getSource() != null ? opportunity.getSource().getCode() : null)
                .campaignCode(opportunity.getCampaign() != null ? opportunity.getCampaign().getCode() : null)
                .currentLevel(opportunity.getCurrentLevel())
                .careerGoal(opportunity.getCareerGoal())
                .painPoint(opportunity.getPainPoint())
                .startTimeline(opportunity.getStartTimeline())
                .preferredChannel(opportunity.getPreferredChannel())
                .fitScore(opportunity.getFitScore())
                .engagementScore(opportunity.getEngagementScore())
                .intentScore(opportunity.getIntentScore())
                .totalScore(opportunity.getTotalScore())
                .leadStage(opportunity.getStatus() != null ? opportunity.getStatus().getCode() : null)
                .ownerId(owner != null ? owner.getId() : null)
                .ownerName(owner != null ? owner.getFullName() : null)
                .lastActivityAt(opportunity.getLastActivityAt())
                .nextActionAt(opportunity.getNextActionAt())
                .doNotContact(opportunity.getDoNotContact())
                .createdAt(opportunity.getCreatedAt())
                .updatedAt(opportunity.getUpdatedAt())
                .build();
    }
}
