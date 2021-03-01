package com.devpies.devpiesback.core.application.domain.dto;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.devpies.devpiesback.core.application.domain.model.AppointmentStatus;
import org.joda.time.DateTime;

import java.util.List;

public class AppointmentDTO {
    User doctor;
    User patient;
    Long id;
    DateTime dateOfAppointment;
    List<String> symptoms;
    AppointmentStatus status;

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(DateTime dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(List<String> symptoms) {
        this.symptoms = symptoms;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
