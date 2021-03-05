package com.devpies.devpiesback.core.application.domain.repository;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface RepresentativeRepository extends JpaRepository<Representative, Long> {
    Optional<Representative> findByUser(User user);
    Optional<Representative> findById(Long id);
    Page<Representative> findAll(Pageable pageable);
}
