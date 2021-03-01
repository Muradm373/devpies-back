package com.devpies.devpiesback.core.rest.services.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.core.application.domain.dto.AppointmentDTO;
import com.devpies.devpiesback.core.application.domain.dto.DoctorDTO;
import com.devpies.devpiesback.core.application.domain.model.Appointment;
import com.devpies.devpiesback.core.application.domain.model.AppointmentStatus;
import com.devpies.devpiesback.core.application.domain.repository.AppointmentRepository;
import com.devpies.devpiesback.core.rest.services.interfaces.IAppointmentService;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AppointmentService implements IAppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AppointmentDTO> getListOfAllAppointments() {
        return appointmentRepository
                .findAll()
                .stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getListOfPatientsAppointments(User user) {
        return appointmentRepository
                .getAllByPatient(user)
                .stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getListOfDoctorsAppointments(User user) {
        return appointmentRepository
                .getAllByDoctor(user)
                .stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getListOfAllAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository
                .getAllByStatus(status)
                .stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getListOfPatientsAppointmentsByStatus(User user, AppointmentStatus status) {
        return appointmentRepository
                .getAllByPatientAndStatus(user, status)
                .stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDTO> getListOfDoctorsAppointmentsByStatus(User user, AppointmentStatus status) {
        return appointmentRepository
                .getAllByDoctorAndStatus(user, status)
                .stream()
                .map(this::convertToAppointmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).get();
    }

    @Override
    public Appointment getAppointmentOfPatientById(User user, Long id) {
        return appointmentRepository.findByIdAndPatient(id, user).get();
    }

    @Override
    public Appointment getAppointmentOfDoctorById(User user, Long id) {
        return appointmentRepository.findByIdAndDoctor(id, user).get();
    }

    @Override
    public AppointmentDTO createAppointment(User from, User to, DateTime dateTime, List<String> symptoms,
                                                  List<String> bodyParts, String description, String questionnaire) {
        Appointment appointment = new Appointment(from, to, dateTime, symptoms, bodyParts,
                description, questionnaire, AppointmentStatus.PENDING);

        appointmentRepository.save(appointment);
        return convertToAppointmentDTO(appointment);
    }

    @Override
    public AppointmentDTO cancelAppointment(User user, Long appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findByIdAndPatient(appointmentId, user);

        if (!appointment.isPresent())
            return null;
        else{
            Appointment cancelledAppointment = appointment.get();
            cancelledAppointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(cancelledAppointment);

            return convertToAppointmentDTO(cancelledAppointment);
        }
    }

    @Override
    public AppointmentDTO rejectAppointment(User user, Long appointmentId, String reason) {
        Optional<Appointment> appointment = appointmentRepository.findByIdAndDoctor(appointmentId, user);

        if (!appointment.isPresent())
            return null;
        else{
            Appointment rejectedAppointment = appointment.get();
            rejectedAppointment.setStatus(AppointmentStatus.REJECTED);
            rejectedAppointment.setRejectionDescription(reason);
            appointmentRepository.save(rejectedAppointment);

            return convertToAppointmentDTO(rejectedAppointment);
        }
    }

    @Override
    public AppointmentDTO acceptAppointment(User user, Long appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findByIdAndDoctor(appointmentId, user);

        if (!appointment.isPresent())
            return null;
        else{
            Appointment acceptedAppointment = appointment.get();
            acceptedAppointment.setStatus(AppointmentStatus.ACTIVE);
            appointmentRepository.save(acceptedAppointment);

            return convertToAppointmentDTO(acceptedAppointment);
        }
    }

    @Override
    public AppointmentDTO endAppointment(User user, Long appointmentId, String result) {
        Optional<Appointment> appointment = appointmentRepository.findByIdAndDoctor(appointmentId, user);

        if (!appointment.isPresent())
            return null;
        else{
            Appointment resolvedAppointment = appointment.get();
            resolvedAppointment.setStatus(AppointmentStatus.RESOLVED);
            resolvedAppointment.setResult(result);
            appointmentRepository.save(resolvedAppointment);

            return convertToAppointmentDTO(resolvedAppointment);
        }
    }

    @Override
    public AppointmentDTO changeDateOfAppointment(User user, Long appointmentId, DateTime newDateTime) {
        Optional<Appointment> appointment = appointmentRepository.findByIdAndPatient(appointmentId, user);

        if (!appointment.isPresent())
            return null;
        else{
            Appointment editedAppointment = appointment.get();
            editedAppointment.setDateOfAppointment(newDateTime);
            appointmentRepository.save(editedAppointment);

            return convertToAppointmentDTO(editedAppointment);
        }
    }

    @Override
    public AppointmentDTO changeDetailsOfAppointment(User user, Long appointmentId, List<String> symptoms, List<String> bodyParts, String description) {
        Optional<Appointment> appointment = appointmentRepository.findByIdAndPatient(appointmentId, user);

        if (!appointment.isPresent())
            return null;
        else{
            Appointment editedAppointment = appointment.get();
            editedAppointment.setSymptoms(symptoms);
            editedAppointment.setBodyParts(bodyParts);
            editedAppointment.setDescription(description);
            appointmentRepository.save(editedAppointment);

            return convertToAppointmentDTO(editedAppointment);
        }
    }

    private AppointmentDTO convertToAppointmentDTO(Appointment appointment){
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        AppointmentDTO appointmentDTO = modelMapper.map(appointment, AppointmentDTO.class);

        return appointmentDTO;
    }
}
