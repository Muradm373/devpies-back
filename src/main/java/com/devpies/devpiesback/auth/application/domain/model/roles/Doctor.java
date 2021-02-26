package com.devpies.devpiesback.auth.application.domain.model.roles;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.core.application.domain.model.Hospital;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;


    @Column(name="phone")
    private String phone;
    @Column(name="homephone")
    private String homephone;
    @Column(name = "name")
    private String name;
    @Column
    private String surname;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_doctor",
            joinColumns = @JoinColumn(
                    name = "doctor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "hospital_doctor",
            joinColumns = @JoinColumn(
                    name = "doctor_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "hospital_id", referencedColumnName = "id"))
    private Hospital hospital;

    public Doctor(Doctor doctor, User user) {
        this.phone = doctor.phone;
        this.homephone = doctor.homephone;
        this.name = doctor.name;
        this.surname = doctor.surname;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHomephone() {
        return homephone;
    }

    public void setHomephone(String homephone) {
        this.homephone = homephone;
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

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
