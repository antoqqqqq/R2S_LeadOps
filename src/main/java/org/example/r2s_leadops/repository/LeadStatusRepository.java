package org.example.r2s_leadops.repository;

import org.example.r2s_leadops.entity.LeadStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadStatusRepository extends JpaRepository<LeadStatus, Long> {
}
