package org.example.r2s_leadops.repository.spec;

import org.example.r2s_leadops.constant.enumarate.LeadSourceEnum;
import org.example.r2s_leadops.constant.enumarate.LeadStage;
import org.example.r2s_leadops.entity.LeadOpportunity;
import org.springframework.data.jpa.domain.Specification;

public final class LeadOpportunitySpecifications {

    private LeadOpportunitySpecifications() {
    }

    public static Specification<LeadOpportunity> hasOwner(Long ownerId) {
        return (root, query, cb) -> ownerId == null
                ? null
                : cb.equal(root.get("assignedUser").get("id"), ownerId);
    }

    public static Specification<LeadOpportunity> hasStage(LeadStage stage) {
        return (root, query, cb) -> stage == null
                ? null
                : cb.equal(root.get("status").get("code"), stage);
    }

    public static Specification<LeadOpportunity> hasSource(LeadSourceEnum source) {
        return (root, query, cb) -> source == null
                ? null
                : cb.equal(root.get("source").get("code"), source);
    }

    public static Specification<LeadOpportunity> searchByPerson(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return null;
            }
            var lead = root.join("lead");
            String pattern = "%" + search.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(lead.get("fullName")), pattern),
                    cb.like(cb.lower(lead.get("phone")), pattern),
                    cb.like(cb.lower(lead.get("email")), pattern)
            );
        };
    }
}
