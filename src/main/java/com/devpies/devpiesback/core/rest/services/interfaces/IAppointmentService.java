package com.devpies.devpiesback.core.rest.services.interfaces;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.core.application.domain.dto.AppointmentDTO;
import com.devpies.devpiesback.core.application.domain.model.Appointment;
import com.devpies.devpiesback.core.application.domain.model.AppointmentStatus;
import org.joda.time.DateTime;

import java.util.List;

public interface IAppointmentService {
    List<AppointmentDTO> getListOfAllAppointments(); //admin
    List<AppointmentDTO> getListOfPatientsAppointments(User user);
    List<AppointmentDTO> getListOfDoctorsAppointments(User user);

    List<AppointmentDTO> getListOfAllAppointmentsByStatus(AppointmentStatus status); //admin
    List<AppointmentDTO> getListOfPatientsAppointmentsByStatus(User user, AppointmentStatus status);
    List<AppointmentDTO> getListOfDoctorsAppointmentsByStatus(User user, AppointmentStatus status);

    Appointment getAppointmentById(Long id); //admin
    Appointment getAppointmentOfPatientById(User user, Long id);
    Appointment getAppointmentOfDoctorById(User user, Long id);

    AppointmentDTO createAppointment(User from, User to, DateTime dateTime, List<String> symptoms,
                              List<String> bodyParts, String description, String questionnaire);
    AppointmentDTO cancelAppointment(User user, Long appointmentId, String reason);
    AppointmentDTO rejectAppointment(User user, Long appointmentId, String reason);
    AppointmentDTO acceptAppointment(User user, Long appointmentId);
    AppointmentDTO endAppointment(User user, Long appointmentId, String result);
    AppointmentDTO changeDateOfAppointment(User user, Long appointmentId, DateTime newDateTime);
    AppointmentDTO changeDetailsOfAppointment(User user, Long appointmentId, List<String> symptoms,
                                       List<String> bodyParts, String description);
}
