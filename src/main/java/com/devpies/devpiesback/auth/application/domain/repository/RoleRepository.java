package com.devpies.devpiesback.auth.application.domain.repository;

import com.devpies.devpiesback.auth.application.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
