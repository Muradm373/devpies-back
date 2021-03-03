package com.devpies.devpiesback.core.application.domain.repository;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.core.application.domain.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUser(User user);
    Optional<Doctor> findById(Long id);
    List<Doctor> findAllByHospital(Hospital hospital);

    @Query("FROM Doctor WHERE hospital.representative = ?1")
    List<Doctor> findAllByRepresentative(Representative representative);

    @Query("SELECT doc FROM Doctor doc WHERE doc.hospital.representative = ?1 and  doc.id = ?2")
    Doctor findByRepresentativeAndId(@Param("representative")Representative representative, @Param("id") Long id);
}
