package com.devpies.devpiesback.auth.application.domain.repository;

import com.devpies.devpiesback.auth.application.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Page<User> findAll(Pageable pageable);

}
