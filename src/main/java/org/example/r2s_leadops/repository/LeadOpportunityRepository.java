package org.example.r2s_leadops.repository;

import org.example.r2s_leadops.entity.LeadOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LeadOpportunityRepository extends JpaRepository<LeadOpportunity, Long> , JpaSpecificationExecutor<LeadOpportunity> {

}
