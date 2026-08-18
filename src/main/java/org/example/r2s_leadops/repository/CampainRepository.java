package org.example.r2s_leadops.repository;

import org.example.r2s_leadops.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampainRepository extends JpaRepository<Campaign, Long> {
}
