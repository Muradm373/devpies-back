package com.devpies.devpiesback.auth.application.service.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.service.interfaces.TokenService;
import com.devpies.devpiesback.auth.application.service.interfaces.UserAuthenticationService;
import com.devpies.devpiesback.auth.application.service.interfaces.UserCrudService;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class TokenAuthenticationService implements UserAuthenticationService {
    @NonNull
    TokenService tokens;
    @NonNull
    UserCrudService users;

    @Override
    public Optional<String> login(final String username, final String password) {
        return users
                .findByUsername(username)
                .filter(user -> Objects.equals(password, user.getPassword()))
                .map(user -> tokens.expiring(ImmutableMap.of("username", username)));
    }

    @Override
    public Optional<User> findByToken(final String token) {
        return Optional
                .of(tokens.verify(token))
                .map(map -> map.get("username"))
                .flatMap(users::findByUsername);
    }

    @Override
    public void logout(final User user) {
        // Nothing to do
    }
}