package org.example.r2s_leadops.repository;

import org.example.r2s_leadops.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
