package com.devpies.devpiesback.core.application.domain.model;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;

@ToString
@Entity
@Table(name="appointments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name="id")
    private User patient;
    private User doctor;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private DateTime dateOfAppointment;
    private List<String> symptoms;
    private List<String> bodyParts;
    private String description;
    private String questionnaire;
    private AppointmentStatus status;
    private String result;

    private String rejectionDescription;

    public Appointment(User patient, User doctor, DateTime dateOfAppointment, List<String> symptoms,
                       List<String> bodyParts, String description, String questionnaire, AppointmentStatus status) {
        this.patient = patient;
        this.doctor = doctor;
        this.dateOfAppointment = dateOfAppointment;
        this.symptoms = symptoms;
        this.bodyParts = bodyParts;
        this.description = description;
        this.questionnaire = questionnaire;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
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

    public List<String> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(List<String> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(String questionnaire) {
        this.questionnaire = questionnaire;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRejectionDescription() {
        return rejectionDescription;
    }

    public void setRejectionDescription(String rejectionDescription) {
        this.rejectionDescription = rejectionDescription;
    }
}
