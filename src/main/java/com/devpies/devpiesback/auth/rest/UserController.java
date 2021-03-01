package com.devpies.devpiesback.auth.rest;


import com.devpies.devpiesback.auth.application.domain.model.Privilege;
import com.devpies.devpiesback.auth.application.domain.model.Role;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.auth.application.domain.repository.RoleRepository;
import com.devpies.devpiesback.auth.application.service.UserAuthenticationService;
import com.devpies.devpiesback.auth.application.service.UserCrudService;
import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.common.config.Roles;
import com.devpies.devpiesback.core.application.domain.repository.PatientRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@FieldDefaults(makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class UserController {
    @NonNull
    UserAuthenticationService authentication;
    @NonNull
    UserCrudService users;
    @NonNull RoleRepository roleRepository;

    @Autowired
    PatientRepository patientRepository;

    @PostMapping("/register")
    String register(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password,  @RequestBody Patient patient) {
        User user = new User(username, username, password);
        user.setRoles(Arrays.asList(roleRepository.findByName(Roles.USER.name())));
        User savedUser = users.save(user);
        Patient savedPatient = patientRepository.save(new Patient(patient, savedUser));

        return login(username, password);
    }

    @PostMapping("/register-admin")
    String registerAdmin(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        User user = new User(username, username, password);
        user.setRoles(Arrays.asList(roleRepository.findByName(Roles.ADMIN.name())));
        users
                .save(user);

        return login(username, password);
    }


    @PostMapping("/login")
    String login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {
        return authentication
                .login(username, password)
                .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    }

}
