package com.devpies.devpiesback.auth.application.domain.model.roles;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.core.application.domain.model.Appointment;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="phone")
    private String phoneNumber;
    @Column(name="homephone")
    private String homePhoneNumber;
    @Column(name = "name")
    private String name;
    @Column
    private String surname;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_patient",
            joinColumns = @JoinColumn(
                    name = "patient_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "patient_appointments",
            joinColumns = @JoinColumn(
                    name = "patient_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "appointment_id", referencedColumnName = "id"))
    private List<Appointment> appointments;

    public Patient(Patient patient, User user) {
        this.appointments = List.of();
        this.homePhoneNumber = patient.homePhoneNumber;
        this.name = patient.name;
        this.surname = patient.surname;
        this.phoneNumber = patient.homePhoneNumber;
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHomePhoneNumber() {
        return homePhoneNumber;
    }

    public void setHomePhoneNumber(String homePhoneNumber) {
        this.homePhoneNumber = homePhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
