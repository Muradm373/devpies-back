package com.devpies.devpiesback.core.application.domain.repository;

import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.devpies.devpiesback.core.application.domain.model.Appointment;
import com.devpies.devpiesback.core.application.domain.model.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> getAllByPatient(Patient patient);
    List<Appointment> getAllByDoctor(Doctor doctor);
    List<Appointment> getAllByStatus(AppointmentStatus status);
    List<Appointment> getAllByDoctorAndStatus(Doctor doctor, AppointmentStatus status);
    List<Appointment> getAllByPatientAndStatus(Patient patient, AppointmentStatus status);
    Optional<Appointment> findById(Long id);
    Optional<Appointment> findByIdAndPatient(Long id, Patient patient);
    Optional<Appointment> findByIdAndDoctor(Long id, Doctor doctor);
    Page<Appointment> findAll(Pageable pageable);
    List<Appointment> findAllByDateOfAppointmentBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Appointment> findAllByDateOfAppointment(LocalDateTime date);
}
