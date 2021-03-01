package com.devpies.devpiesback.core.rest.services.interfaces;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.devpies.devpiesback.core.application.domain.dto.AppointmentDTO;
import com.devpies.devpiesback.core.application.domain.model.Appointment;
import com.devpies.devpiesback.core.application.domain.model.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {
    List<AppointmentDTO> getListOfAllAppointments(); //admin
    List<AppointmentDTO> getListOfPatientsAppointments(Patient user);
    List<AppointmentDTO> getListOfDoctorsAppointments(Doctor user);

    List<AppointmentDTO> getListOfAllAppointmentsByStatus(AppointmentStatus status); //admin
    List<AppointmentDTO> getListOfPatientsAppointmentsByStatus(Patient user, AppointmentStatus status);
    List<AppointmentDTO> getListOfDoctorsAppointmentsByStatus(Doctor user, AppointmentStatus status);

    Appointment getAppointmentById(Long id); //admin
    Appointment getAppointmentOfPatientById(Patient user, Long id);
    Appointment getAppointmentOfDoctorById(Doctor user, Long id);

    AppointmentDTO createAppointment(Patient from, Doctor to, LocalDateTime dateTime, List<String> symptoms,
                                     List<String> bodyParts, String description, String questionnaire);
    AppointmentDTO cancelAppointment(Patient user, Long appointmentId);
    AppointmentDTO rejectAppointment(Doctor user, Long appointmentId, String reason);
    AppointmentDTO acceptAppointment(Doctor user, Long appointmentId);
    AppointmentDTO endAppointment(Doctor user, Long appointmentId, String result);
    AppointmentDTO changeDateOfAppointment(Patient user, Long appointmentId, LocalDateTime newDateTime);
    AppointmentDTO changeDetailsOfAppointment(Patient user, Long appointmentId, List<String> symptoms,
                                       List<String> bodyParts, String description);
}
