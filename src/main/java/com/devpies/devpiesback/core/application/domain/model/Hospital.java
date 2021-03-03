package com.devpies.devpiesback.core.application.domain.model;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="hospitals")
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @JoinTable(
            name = "hospital_representative",
            joinColumns = @JoinColumn(
                    name = "hospital_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "representative_id", referencedColumnName = "id"))
    private Representative representative;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime openTime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime closeTime;
    private String phoneNumber;
    private String name;
    int rating;
    private String website;
    private String address;

    @OneToMany
    @JoinTable(
            name = "hospital_doctors",
            joinColumns = @JoinColumn(
                    name = "hospital_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "doctor_id", referencedColumnName = "id"))
    private List<Doctor> doctors;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonManagedReference
    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalDateTime openTime) {
        this.openTime = openTime;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
