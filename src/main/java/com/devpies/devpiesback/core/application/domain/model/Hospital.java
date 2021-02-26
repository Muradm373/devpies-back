package com.devpies.devpiesback.core.application.domain.model;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import org.joda.time.DateTime;

import javax.persistence.*;
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
    private DateTime openTime, closeTime;
    private String phoneNumber;
    int rating;
    String website;

    @OneToMany
    @JoinTable(
            name = "hospital_doctors",
            joinColumns = @JoinColumn(
                    name = "hospital_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "doctor_id", referencedColumnName = "id"))
    private List<Doctor> doctors;

    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }

    public DateTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(DateTime openTime) {
        this.openTime = openTime;
    }

    public DateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(DateTime closeTime) {
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
