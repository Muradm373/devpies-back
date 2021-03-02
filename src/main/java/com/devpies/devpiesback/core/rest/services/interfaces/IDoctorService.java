package com.devpies.devpiesback.core.rest.services.interfaces;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.core.application.domain.dto.DoctorDTO;
import com.devpies.devpiesback.core.application.domain.model.Hospital;

import java.util.List;

public interface IDoctorService {
     List<DoctorDTO> getAllDoctorsDTO();
     DoctorDTO addDoctorToHospital(Doctor doctor, Hospital hospital, String username, String password);
     Doctor getDoctorByIdAndRepresentative(Long id, Representative representative);
     DoctorDTO getDoctorDTOById(Long id);
     Doctor getDoctorById(Long id); //admin
     List<DoctorDTO> getDoctorsByHospital(Hospital hospital);
     List<DoctorDTO> getDoctorsByRepresentative(Representative representative);
     DoctorDTO editDoctorByIdAndRepresentative(Long id, Representative representative, Doctor updatedDoctor);
     DoctorDTO editDoctorById(Long id, Doctor updatedDoctor); //admin
     Boolean deleteDoctorByIdAndRepresentative(Long id, Representative representative);
     Boolean deleteDoctorById(Long id); //admin
     Doctor getDoctorByUser(User user);

}
