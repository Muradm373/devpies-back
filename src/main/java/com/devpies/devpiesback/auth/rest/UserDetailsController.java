package com.devpies.devpiesback.auth.rest;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.repository.RoleRepository;
import com.devpies.devpiesback.auth.application.service.UserAuthenticationService;
import com.devpies.devpiesback.auth.application.service.UserCrudService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;

@CrossOrigin
@RestController
@RequestMapping("/")
@FieldDefaults(makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class UserDetailsController {
    @NonNull
    UserAuthenticationService authentication;
    @NonNull
    UserCrudService users;
    @NonNull RoleRepository roleRepository;

    @RequestMapping(value = "/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> getCurrent(@AuthenticationPrincipal final User user) {
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping("/logout")
    boolean logout(@AuthenticationPrincipal final User user) {
        authentication.logout(user);
        return true;
    }
}
