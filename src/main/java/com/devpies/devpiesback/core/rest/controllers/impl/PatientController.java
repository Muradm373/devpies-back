package com.devpies.devpiesback.core.rest.controllers.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.devpies.devpiesback.auth.application.service.interfaces.UserCrudService;
import com.devpies.devpiesback.core.application.domain.dto.AppointmentDTO;
import com.devpies.devpiesback.core.application.domain.dto.DoctorDTO;
import com.devpies.devpiesback.core.application.domain.dto.HospitalDTO;
import com.devpies.devpiesback.core.application.domain.model.Appointment;
import com.devpies.devpiesback.core.application.domain.model.AppointmentStatus;
import com.devpies.devpiesback.core.application.domain.model.Hospital;
import com.devpies.devpiesback.core.rest.services.impl.AppointmentService;
import com.devpies.devpiesback.core.rest.services.impl.DoctorService;
import com.devpies.devpiesback.core.rest.services.impl.HospitalService;
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
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    AppointmentService appointmentService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    PatientService patientService;
    @Autowired
    HospitalService hospitalService;
    @Autowired
    UserCrudService userCrudService;

    @RequestMapping(value = "appointments", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDTO> createAppointment(@AuthenticationPrincipal final User user,@RequestBody AppointmentDTO appointment){

        System.out.println(appointment.getSymptoms());
        Doctor doctor = doctorService.getDoctorById(appointment.getDoctorId());
        if(doctor == null) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        Patient patient = patientService.findPatientByUser(user);
        AppointmentDTO appointmentDTO = appointmentService.createAppointment(patient, doctor, appointment.getDateOfAppointment(),
                appointment.getSymptoms(), appointment.getBodyParts(),
                appointment.getDescription(), appointment.getQuestionnaire());

        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AppointmentDTO>> getAppointments(@AuthenticationPrincipal final User user){

        Patient patient = patientService.findPatientByUser(user);
        List<AppointmentDTO> appointments = appointmentService.getListOfPatientsAppointments(patient);
        if(appointments == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Appointment> getAppointment(@AuthenticationPrincipal final User user,  @PathVariable("id") final Long id){

        Patient patient = patientService.findPatientByUser(user);
        Appointment appointment = appointmentService.getAppointmentOfPatientById(patient, id);
        if(appointment == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/{id}", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDTO> cancelAppointment(@AuthenticationPrincipal final User user,  @PathVariable("id") final Long id){

        Patient patient = patientService.findPatientByUser(user);
        Appointment appointment = appointmentService.getAppointmentOfPatientById(patient, id);
        if(appointment == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);


        AppointmentDTO appointmentDTO = appointmentService.cancelAppointment(patient, id);

        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/{id}", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDTO> changeDateOfAppointment(@AuthenticationPrincipal final User user, @PathVariable("id") final Long id,
                                                           @RequestBody AppointmentDTO appointmentChanged){
        Patient patient = patientService.findPatientByUser(user);
        Appointment appointment = appointmentService.getAppointmentOfPatientById(patient, id);
        if(appointment == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        AppointmentDTO appointmentDTO = appointmentService.changeDateOfAppointment(patient, id, appointmentChanged.getDateOfAppointment());

        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/{id}", method= RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<AppointmentDTO> changeDetailsOfAppointment(@AuthenticationPrincipal final User user, @PathVariable("id") final Long id,
                                                              @RequestBody Appointment appointmentChanged){
        Patient patient = patientService.findPatientByUser(user);
        Appointment appointment = appointmentService.getAppointmentOfPatientById(patient, id);
        if(appointment == null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        AppointmentDTO appointmentDTO = appointmentService.changeDetailsOfAppointment(patient, id,
                appointmentChanged.getSymptoms(),appointmentChanged.getBodyParts(), appointmentChanged.getDescription());

        return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "appointments/status", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<AppointmentDTO>> getAppointmentsByStatus(@AuthenticationPrincipal final User user, @RequestParam("status") String status){
        Patient patient = patientService.findPatientByUser(user);
        if(patient == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        List<AppointmentDTO> appointments = appointmentService.getListOfPatientsAppointmentsByStatus(patient, AppointmentStatus.decode(status));
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }


    @RequestMapping(value = "doctors", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DoctorDTO>> getDoctorsList(@AuthenticationPrincipal final User user){
        Patient patient = patientService.findPatientByUser(user);
        if(patient == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        List<DoctorDTO> doctors = doctorService.getAllDoctorsDTO();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @RequestMapping(value = "doctors/page", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DoctorDTO>> getAllDoctorsByPage(@AuthenticationPrincipal final User user, @RequestParam("page") Integer page){
        return new ResponseEntity<>(doctorService.getAllDoctorsByPage(page), HttpStatus.OK);
    }
    @RequestMapping(value = "hospitals/page", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<HospitalDTO>> getAllHospitalsByPage(@AuthenticationPrincipal final User user, @RequestParam("page") Integer page){
        return new ResponseEntity<>(hospitalService.getAllHospitalsByPage(page), HttpStatus.OK);
    }

    @RequestMapping(value = "hospitals/doctors/page", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DoctorDTO>> getDoctorsByHospitalAndPage(@AuthenticationPrincipal final User user,
                                                         @RequestParam("id") Long hospitalId, @RequestParam("page") Integer page){
        return new ResponseEntity<>(doctorService.getAllDoctorsByPageAndHospitalId(page, hospitalId), HttpStatus.OK);
    }

    @RequestMapping(value = "hospitals/doctors", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<DoctorDTO>> getDoctorsByHospital(@AuthenticationPrincipal final User user,
                                                         @RequestParam("id") Long hospitalId){
        Patient patient = patientService.findPatientByUser(user);
        if(patient == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        Hospital hospital = hospitalService.getHospitalById(hospitalId);
        if(hospital == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        List<DoctorDTO> doctors = doctorService.getDoctorsByHospital(hospital);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }



    @RequestMapping(value = "doctors/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<DoctorDTO> getDoctorById(@AuthenticationPrincipal final User user,
                                                  @PathVariable("id") final Long id){
        Patient patient = patientService.findPatientByUser(user);
        if(patient == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        DoctorDTO doctor = doctorService.getDoctorDTOById(id);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    @RequestMapping(value = "hospitals", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<HospitalDTO>> getListOfHospitals(@AuthenticationPrincipal final User user){
        Patient patient = patientService.findPatientByUser(user);
        if(patient == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        List<HospitalDTO> listOfHospitals = hospitalService.getAllHospitalsDTO();
        return new ResponseEntity<>(listOfHospitals, HttpStatus.OK);
    }

    @RequestMapping(value = "hospitals/{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Hospital> getHospitalById(@AuthenticationPrincipal final User user,
                                            @PathVariable("id") final Long id){
        Patient patient = patientService.findPatientByUser(user);
        if(patient == null)
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        Hospital hospital = hospitalService.getHospitalById(id);
        return new ResponseEntity<>(hospital, HttpStatus.OK);
    }

    @RequestMapping(value = "profile", method= RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> deletePatient(@AuthenticationPrincipal final User user){
        return new ResponseEntity<>(patientService.deletePatient(user), HttpStatus.OK);
    }

    @RequestMapping(value = "profile", method= RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Boolean> editPatient(@AuthenticationPrincipal final User user,@RequestBody Patient patient){
        return new ResponseEntity<>(patientService.editPatient(user, patient), HttpStatus.OK);
    }

    @RequestMapping(value = "profile", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Patient> getPatientInfo(@AuthenticationPrincipal final User user){
        return new ResponseEntity<>(patientService.getPatientByUser(user), HttpStatus.OK);
    }



}
