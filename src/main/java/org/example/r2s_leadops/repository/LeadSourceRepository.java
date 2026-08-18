package org.example.r2s_leadops.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.r2s_leadops.entity.LeadSource;

public interface LeadSourceRepository extends JpaRepository<LeadSource, Long> {
}
