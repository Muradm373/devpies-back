package com.devpies.devpiesback.core.application.domain.repository;

import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.core.application.domain.model.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    Optional<Hospital> findById(Long id);
    Optional<List<Hospital>> findAllByRepresentative(Representative representative);
    Optional<Hospital> findByIdAndRepresentative(Long id, Representative representative);
    Page<Hospital> findAll(Pageable pageable);
}
