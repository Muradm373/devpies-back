package com.devpies.devpiesback.core.rest.controllers.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.core.application.domain.dto.AppointmentDTO;
import com.devpies.devpiesback.core.application.domain.model.Appointment;
import com.devpies.devpiesback.core.application.domain.model.AppointmentStatus;
import com.devpies.devpiesback.core.rest.services.impl.AppointmentService;
import com.devpies.devpiesback.core.rest.services.impl.DoctorService;
import com.devpies.devpiesback.core.rest.services.impl.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    PatientService patientService;
    @Autowired
    DoctorService doctorService;


    @RequestMapping(value = "appointments", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AppointmentDTO>> getAppointments(@AuthenticationPrincipal final User user){
        Doctor doctor = doctorService.getDoctorByUser(user);
        if(doctor == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        List<AppointmentDTO> appointments = appointmentService.getListOfDoctorsAppointments(doctor);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/status", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AppointmentDTO>> getAppointmentsByStatus(@AuthenticationPrincipal final User user,
                                                                 @RequestParam("status") String status){
        Doctor doctor = doctorService.getDoctorByUser(user);
        if(doctor == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        List<AppointmentDTO> appointments = appointmentService.getListOfDoctorsAppointmentsByStatus(doctor, AppointmentStatus.decode(status));
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Appointment> getAppointmentById(@AuthenticationPrincipal final User user, @PathVariable("id") final Long id){
        Doctor doctor = doctorService.getDoctorByUser(user);
        if(doctor == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        Appointment appointment = appointmentService.getAppointmentOfDoctorById(doctor, id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/{id}", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDTO> acceptAppointment(@AuthenticationPrincipal final User user, @PathVariable("id") final Long id){
        Doctor doctor = doctorService.getDoctorByUser(user);
        if(doctor == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        AppointmentDTO appointment = appointmentService.acceptAppointment(doctor, id);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/{id}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDTO> rejectAppointment(@AuthenticationPrincipal final User user, @PathVariable("id") final Long id,
                                                     @RequestBody String reason){
        Doctor doctor = doctorService.getDoctorByUser(user);
        if(doctor == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        AppointmentDTO appointment = appointmentService.rejectAppointment(doctor, id, reason);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/{id}", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDTO> endAppointment(@AuthenticationPrincipal final User user, @PathVariable("id") final Long id,
                                                     @RequestBody String result){
        Doctor doctor = doctorService.getDoctorByUser(user);
        if(doctor == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        AppointmentDTO appointment = appointmentService.endAppointment(doctor, id, result);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @RequestMapping(value = "profile", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> deleteDoctor(@AuthenticationPrincipal final User user){
        return new ResponseEntity<>(doctorService.deleteDoctor(user), HttpStatus.OK);
    }

    @RequestMapping(value = "profile", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> editDoctor(@AuthenticationPrincipal final User user,@RequestBody Doctor doctor){
        return new ResponseEntity<>(doctorService.editDoctor(user, doctor), HttpStatus.OK);
    }

    @RequestMapping(value = "profile", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Doctor> getDoctorInfo(@AuthenticationPrincipal final User user){
        return new ResponseEntity<>(doctorService.getDoctorByUser(user), HttpStatus.OK);
    }




}
