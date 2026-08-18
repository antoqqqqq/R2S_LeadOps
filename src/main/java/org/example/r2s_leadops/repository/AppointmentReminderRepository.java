package org.example.r2s_leadops.repository;

import org.example.r2s_leadops.entity.AppointmentReminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentReminderRepository extends JpaRepository<AppointmentReminder, Long> {
}
