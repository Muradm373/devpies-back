package com.devpies.devpiesback.auth.application.service;

import com.devpies.devpiesback.auth.application.domain.model.User;

import java.util.Optional;

public interface UserCrudService {
    User save(User user);
    Optional<User> find(Long id);
    Optional<User> findByUsername(String username);
}
