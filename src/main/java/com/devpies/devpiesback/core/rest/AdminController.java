package com.devpies.devpiesback.core.rest;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.core.application.domain.repository.RepresentativeRepository;
import com.devpies.devpiesback.auth.application.domain.repository.RoleRepository;
import com.devpies.devpiesback.auth.application.service.UserAuthenticationService;
import com.devpies.devpiesback.auth.application.service.UserCrudService;
import com.devpies.devpiesback.common.config.Roles;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired RoleRepository roleRepository;
    @NonNull
    UserAuthenticationService authentication;
    @Autowired
    UserCrudService users;
    @Autowired
    RepresentativeRepository representativeRepository;

    @RequestMapping(value = "representatives", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Representative> addRepresentativeUser(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password,
            @RequestBody Representative representative){
        User user = new User(username, username, password, roleRepository.findByName(Roles.REPRESENTATIVE.name()));
        User savedUser = users.save(user);
        Representative savedRepresentative = representativeRepository.save(new Representative(representative, savedUser));

        return new ResponseEntity<>(savedRepresentative, HttpStatus.OK);
    }

    @RequestMapping(value = "representatives", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Representative>> getRepresentatives(){
        return new ResponseEntity<>(representativeRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "representatives/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Optional<Representative>> getRepresentativeById(@PathVariable Long id){
        return new ResponseEntity<>(representativeRepository.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "representatives/{id]/user", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<User> getRepresentativeUser(@PathVariable Long id){
        return new ResponseEntity<>(representativeRepository.findById(id).get().getUser(), HttpStatus.OK);
    }

}
