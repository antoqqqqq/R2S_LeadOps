package org.example.r2s_leadops.repository;

import org.example.r2s_leadops.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
