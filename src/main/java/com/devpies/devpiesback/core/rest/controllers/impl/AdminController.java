package com.devpies.devpiesback.core.rest.controllers.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.auth.application.service.impl.UserService;
import com.devpies.devpiesback.core.application.domain.dto.*;
import com.devpies.devpiesback.core.application.domain.model.Appointment;
import com.devpies.devpiesback.core.application.domain.model.AppointmentStatus;
import com.devpies.devpiesback.core.application.domain.repository.PatientRepository;
import com.devpies.devpiesback.core.application.domain.repository.RepresentativeRepository;
import com.devpies.devpiesback.auth.application.domain.repository.RoleRepository;
import com.devpies.devpiesback.auth.application.service.interfaces.UserAuthenticationService;
import com.devpies.devpiesback.auth.application.service.interfaces.UserCrudService;
import com.devpies.devpiesback.common.config.Roles;
import com.devpies.devpiesback.core.rest.services.impl.*;
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
    PatientRepository patientRepository;

    @Autowired
    RepresentativeService representativeService;
    @Autowired
    UserService userService;
    @Autowired
    PatientService patientService;
    @Autowired
    HospitalService hospitalService;

    @Autowired
    AppointmentService appointmentService;
    @Autowired
    DoctorService doctorService;

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
    ResponseEntity<Optional<Representative>> getRepresentativeById(@PathVariable("id") Long id){
        return new ResponseEntity<>(representativeRepository.findById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "representatives/{id}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> removeRepresentativeById(@PathVariable("id") Long id){
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
    ResponseEntity<List<PatientDTO>> getAllUsers(){
        return new ResponseEntity<>(patientService.getAllPatientsDTO(), HttpStatus.OK);
    }

    @RequestMapping(value = "users/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Patient>getPatientById(@PathVariable("id") Long id){
        Patient patient = patientService.getPatientById(id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @RequestMapping(value = "users/{id}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> removePatientById(@PathVariable("id") Long id){
        Patient patient = patientRepository.findById(id).get();
        patientRepository.delete(patient);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AppointmentDTO>> getAppointments(@AuthenticationPrincipal final User user){
        return new ResponseEntity<>(appointmentService.getListOfAllAppointments(), HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/status", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AppointmentDTO>> getAppointmentsByStatus(@AuthenticationPrincipal final User user,
                                                                 @RequestParam("status") String status){
        return new ResponseEntity<>(appointmentService.getListOfAllAppointmentsByStatus(AppointmentStatus.decode(status)), HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Appointment> getAppointmentsById(@AuthenticationPrincipal final User user,
                                                        @PathVariable("id") final Long id){
        return new ResponseEntity<>(appointmentService.getAppointmentById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "doctors", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DoctorDTO>> getAllDoctors(@AuthenticationPrincipal final User user){
        return new ResponseEntity<>(doctorService.getAllDoctorsDTO(), HttpStatus.OK);
    }

    @RequestMapping(value = "doctors/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Doctor> getDoctorById(@AuthenticationPrincipal final User user,
                                             @PathVariable("id") final Long id ){
        Doctor result = doctorService.getDoctorById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "doctors/{id}", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DoctorDTO> editDoctorById(@AuthenticationPrincipal final User user,
                                             @PathVariable("id") final Long id, @RequestBody Doctor doctorNew ){
        DoctorDTO result = doctorService.editDoctorById(id, doctorNew);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "doctors/{id}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> deleteDoctorById(@AuthenticationPrincipal final User user,
                                             @PathVariable("id") final Long id ){
        Boolean result = doctorService.deleteDoctorById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "doctors/page", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DoctorDTO>> getAllDoctorsByPage(@AuthenticationPrincipal final User user, @RequestParam("page") Integer page){
        return new ResponseEntity<>(doctorService.getAllDoctorsByPage(page), HttpStatus.OK);
    }

    @RequestMapping(value = "representatives/page", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<RepresentativeDTO>> getRepresentativesByPage(@RequestParam("page") Integer page){
        return new ResponseEntity<>(representativeService.getRepresentativesByPage(page), HttpStatus.OK);
    }

    @RequestMapping(value = "users/page", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<PatientDTO>> getAllUsersByPage(@AuthenticationPrincipal final User user, @RequestParam("page") Integer page){
        return new ResponseEntity<>(patientService.getPatientByPage(page), HttpStatus.OK);
    }

    @RequestMapping(value = "hospitals/page", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<HospitalDTO>> getAllHospitalsByPage(@AuthenticationPrincipal final User user, @RequestParam("page") Integer page){
        return new ResponseEntity<>(hospitalService.getAllHospitalsByPage(page), HttpStatus.OK);
    }


}
