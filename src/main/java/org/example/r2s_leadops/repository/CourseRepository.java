package org.example.r2s_leadops.repository;

import org.example.r2s_leadops.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
