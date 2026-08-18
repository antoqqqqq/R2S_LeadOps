package org.example.r2s_leadops.repository;

import org.example.r2s_leadops.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
