package com.devpies.devpiesback.core.rest;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.auth.application.service.impl.UserService;
import com.devpies.devpiesback.core.application.domain.dto.RepresentativeDTO;
import com.devpies.devpiesback.core.application.domain.dto.UserDTO;
import com.devpies.devpiesback.core.application.domain.repository.RepresentativeRepository;
import com.devpies.devpiesback.auth.application.domain.repository.RoleRepository;
import com.devpies.devpiesback.auth.application.service.UserAuthenticationService;
import com.devpies.devpiesback.auth.application.service.UserCrudService;
import com.devpies.devpiesback.common.config.Roles;
import com.devpies.devpiesback.core.rest.services.RepresentativeService;
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

    @Autowired
    RepresentativeService representativeService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "representatives", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Representative> addRepresentativeUser(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password,
            @RequestBody Representative representative){

        if(users.findByUsername(representative.getName()).isPresent())
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        User user = new User(username, username, password, roleRepository.findByName(Roles.REPRESENTATIVE.name()));
        User savedUser = users.save(user);
        Representative savedRepresentative = representativeRepository.save(new Representative(representative, savedUser));

        return new ResponseEntity<>(savedRepresentative, HttpStatus.OK);
    }

    @RequestMapping(value = "representatives", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<RepresentativeDTO>> getRepresentatives(){
        return new ResponseEntity<>(representativeService.getAllRepresentativesDTO(), HttpStatus.OK);
    }

    @RequestMapping(value = "representatives/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Optional<Representative>> getRepresentativeById(@PathVariable Long id){
        return new ResponseEntity<>(representativeRepository.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "representatives/{id}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> removeRepresentativeById(@PathVariable Long id){
        Representative representative = representativeRepository.getOne(id);
        representativeRepository.delete(representative);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    @RequestMapping(value = "representatives", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Representative> updateRepresentative( @RequestBody Representative representative){
        Representative representativeUpd = representativeRepository.getOne(representative.getId());
        representativeUpd.setHomephone(representative.getHomephone());
        representativeUpd.setName(representative.getName());
        representativeUpd.setSurname(representative.getSurname());

        User userUpd = representativeUpd.getUser();
        userUpd.setEmail(representative.getUser().getEmail());
        userUpd.setUsername(representative.getUser().getUsername());
        representativeUpd.setUser(userUpd);

        return new ResponseEntity<>(representativeRepository.save(representativeUpd), HttpStatus.OK);
    }

    @RequestMapping(value = "users", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<UserDTO>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

}
