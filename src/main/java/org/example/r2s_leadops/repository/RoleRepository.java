package org.example.r2s_leadops.repository;

import java.util.Optional;
import org.example.r2s_leadops.constant.enumarate.UserRole;
import org.example.r2s_leadops.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(UserRole code);
}
