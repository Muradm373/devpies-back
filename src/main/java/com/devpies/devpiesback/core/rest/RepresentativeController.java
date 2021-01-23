package com.devpies.devpiesback.core.rest;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.core.application.domain.model.Hospital;
import com.devpies.devpiesback.core.application.domain.repository.DoctorRepository;
import com.devpies.devpiesback.auth.application.domain.repository.RoleRepository;
import com.devpies.devpiesback.auth.application.service.UserAuthenticationService;
import com.devpies.devpiesback.auth.application.service.UserCrudService;
import com.devpies.devpiesback.common.config.Roles;
import com.devpies.devpiesback.core.application.domain.repository.HospitalRepository;
import com.devpies.devpiesback.core.application.domain.repository.RepresentativeRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/representative")
public class RepresentativeController {
    @Autowired
    RoleRepository roleRepository;
    @NonNull
    UserAuthenticationService authentication;
    @Autowired
    UserCrudService users;
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    RepresentativeRepository representativeRepository;
    @Autowired
    HospitalRepository hospitalRepository;

    @RequestMapping(value = "hospitals/{id}", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Doctor> addDoctor(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password,
            @PathVariable("id") final Long id,
            @RequestBody Doctor doctor){
        User user = new User(username, username, password, roleRepository.findByName(Roles.DOCTOR.name()));
        User savedUser = users.save(user);
        Doctor savedDoctor = doctorRepository.save(new Doctor(doctor, savedUser));

        return new ResponseEntity<>(savedDoctor, HttpStatus.OK);
    }


    @RequestMapping(value = "hospitals", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Hospital> addHospital(@AuthenticationPrincipal final User user,
            @RequestBody Hospital hospital){
        Representative representative = representativeRepository.findByUser(user).get();
        hospital.setRepresentative(representative);
        Hospital savedHospital = hospitalRepository.save(hospital);
        return new ResponseEntity<>(savedHospital, HttpStatus.OK);
    }

    @RequestMapping(value = "hospitals", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Optional<List<Hospital>>> getHospitals(@AuthenticationPrincipal final User user){
        Representative representative = representativeRepository.findByUser(user).get();
        return new ResponseEntity<>(hospitalRepository.findAllByRepresentative(representative), HttpStatus.OK);
    }
}
