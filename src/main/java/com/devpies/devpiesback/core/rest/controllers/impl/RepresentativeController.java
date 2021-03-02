package com.devpies.devpiesback.core.rest.controllers.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.core.application.domain.dto.DoctorDTO;
import com.devpies.devpiesback.core.application.domain.dto.HospitalDTO;
import com.devpies.devpiesback.core.application.domain.model.Hospital;
import com.devpies.devpiesback.core.application.domain.repository.HospitalRepository;
import com.devpies.devpiesback.core.rest.services.impl.DoctorService;
import com.devpies.devpiesback.core.rest.services.impl.HospitalService;
import com.devpies.devpiesback.core.rest.services.impl.RepresentativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/representative")
public class RepresentativeController {
    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    HospitalService hospitalService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    RepresentativeService representativeService;

    @RequestMapping(value = "hospitals/{id}", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DoctorDTO> addDoctor(
            @AuthenticationPrincipal final User user,
            @RequestParam("username") final String username,
            @RequestParam("password") final String password,
            @PathVariable("id") final Long id,
            @RequestBody Doctor doctor){

        Hospital hospital = hospitalService.getHospitalByIdAndRepresentative(id, user);
        if(hospital == null){
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        DoctorDTO doctorDTO = doctorService.addDoctorToHospital(doctor, hospital, username, password);

        return new ResponseEntity<>(doctorDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "hospitals/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Hospital> getHospitalById(@AuthenticationPrincipal final User user,
            @PathVariable("id") final Long id){
        Hospital hospital = hospitalService.getHospitalByIdAndRepresentative(id, user);

        return new ResponseEntity<>(hospital, HttpStatus.OK);
    }


    @RequestMapping(value = "hospitals", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<HospitalDTO> addHospital(@AuthenticationPrincipal final User user,
            @RequestBody Hospital hospital ){
        Representative representative = representativeService.findByUser(user);
        hospital.setRepresentative(representative);
        Hospital savedHospital = hospitalRepository.save(hospital);
        return new ResponseEntity<>(hospitalService.getHospitalDTO(savedHospital.getId()), HttpStatus.OK);
    }

    @RequestMapping(value = "hospitals/{id}", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<HospitalDTO> editHospital(@AuthenticationPrincipal final User user,
                                            @RequestBody Hospital hospital, @PathVariable("id") final Long id ){
        HospitalDTO updatedHospital = hospitalService.updateHospitalByRepresentativeAndId(id, hospital,user);
        if(updatedHospital == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(updatedHospital, HttpStatus.OK);
    }

    @RequestMapping(value = "hospitals/{id}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> removeHospital(@AuthenticationPrincipal final User user, @PathVariable("id") final Long id ){
        Boolean deletedHospital = hospitalService.deleteHospitalByIdAndRepresentative(id,user);
        if(!deletedHospital)
            return new ResponseEntity<>("Not deleted", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @RequestMapping(value = "hospitals", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<HospitalDTO>> getHospitals(@AuthenticationPrincipal final User user){
        Representative representative = representativeService.findByUser(user);
        return new ResponseEntity<>(hospitalService.getAllHospitalsDTO(representative), HttpStatus.OK);
    }

    @RequestMapping(value = "doctors", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DoctorDTO>> getDoctors(@AuthenticationPrincipal final User user){
        Representative representative = representativeService.findByUser(user);
        if(representative == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        List<DoctorDTO> doctors = doctorService.getDoctorsByRepresentative(representative);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @RequestMapping(value = "doctors/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Doctor> getDoctorById(@AuthenticationPrincipal final User user,
                                                  @PathVariable("id") final Long id ){
        Representative representative = representativeService.findByUser(user);
        if(representative == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        Doctor doctor = doctorService.getDoctorByIdAndRepresentative(id, representative);
        if(representative == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @RequestMapping(value = "doctors/{id}", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DoctorDTO> editDoctorById(@AuthenticationPrincipal final User user,
                                         @PathVariable("id") final Long id, @RequestBody Doctor doctorNew ){
        Representative representative = representativeService.findByUser(user);
        if(representative == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        DoctorDTO result = doctorService.editDoctorByIdAndRepresentative(id, representative, doctorNew);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "doctors/{id}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> deleteDoctorById(@AuthenticationPrincipal final User user,
                                          @PathVariable("id") final Long id ){
        Representative representative = representativeService.findByUser(user);
        if(representative == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        Boolean result = doctorService.deleteDoctorByIdAndRepresentative(id, representative);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
