package org.example.r2s_leadops.repository;

import org.example.r2s_leadops.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead, Long> {
}
