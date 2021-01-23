package com.devpies.devpiesback.auth.application.service;

import com.devpies.devpiesback.auth.application.domain.model.User;

import java.util.Optional;

public interface UserAuthenticationService {
    Optional<String> login(String username, String password);
    Optional<User> findByToken(String token);
    void logout(User user);

}
