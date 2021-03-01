package com.devpies.devpiesback.core.application.domain.model;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="appointments")
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_appointment",
            joinColumns = @JoinColumn(
                    name = "appointment_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private Patient patient;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "doctor_appointment",
            joinColumns = @JoinColumn(
                    name = "appointment_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "doctor_id", referencedColumnName = "id"))
    private Doctor doctor;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime dateOfAppointment;
    @ElementCollection
    private List<String> symptoms;
    @ElementCollection
    private List<String> bodyParts;
    private String description;
    private String questionnaire;
    private AppointmentStatus status;
    private String result;

    private String rejectionDescription;

    public Appointment(Patient patient, Doctor doctor, LocalDateTime dateOfAppointment, List<String> symptoms,
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(LocalDateTime dateOfAppointment) {
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
