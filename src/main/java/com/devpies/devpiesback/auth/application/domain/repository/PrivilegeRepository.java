package com.devpies.devpiesback.auth.application.domain.repository;

import com.devpies.devpiesback.auth.application.domain.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);
}
