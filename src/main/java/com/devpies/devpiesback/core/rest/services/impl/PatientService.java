package com.devpies.devpiesback.core.rest.services.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.auth.application.service.interfaces.UserCrudService;
import com.devpies.devpiesback.core.application.domain.dto.PatientDTO;
import com.devpies.devpiesback.core.application.domain.repository.PatientRepository;
import com.devpies.devpiesback.core.rest.services.interfaces.IPatientService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientService implements IPatientService {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    UserCrudService userCrudService;

    public List<PatientDTO> getAllPatientsDTO(){
        return (patientRepository
                .findAll(PageRequest.of(0, Integer.MAX_VALUE))
                .stream()
                .map(this::convertToPatientDTO).collect(Collectors.toList()));
    }

    public Patient findPatientByUser(User user){
        return patientRepository.findByUser(user).get();
    }

    private PatientDTO convertToPatientDTO(Patient patient){
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setEmail(patient.getUser().getEmail());
        patientDTO.setId(patient.getId());
        patientDTO.setName(patient.getName());
        patientDTO.setSurname(patient.getSurname());
        patientDTO.setPhone(patient.getPhoneNumber());
        patientDTO.setCity(patient.getCity());
        patientDTO.setCountry(patient.getCountry());
        patientDTO.setIdNumber(patient.getIdNumber());
        return patientDTO;
    }

    @Override
    public Boolean deletePatient(User user) {
        Optional<User> fUser = userCrudService.find(user.getId());
        if(!fUser.isPresent())
            return false;
        Optional<Patient> patient = patientRepository.findByUser(fUser.get());
        if(!patient.isPresent())
            return false;
        userCrudService.deleteUser(fUser.get());
        patientRepository.delete(patient.get());

        return true;
    }

    @Override
    public Boolean editPatient(User user, Patient patientNew) {
        Optional<User> fUser = userCrudService.find(user.getId());
        if(!fUser.isPresent())
            return false;
        Optional<Patient> patient = patientRepository.findByUser(fUser.get());
        if(!patient.isPresent())
            return false;

        Patient patientUpdated = patient.get();
        patientUpdated.setHomePhoneNumber(patientNew.getHomePhoneNumber());
        patientUpdated.setName(patientNew.getName());
        patientUpdated.setSurname(patientNew.getSurname());
        patientUpdated.setPhoneNumber(patientNew.getPhoneNumber());
        patientUpdated.setCity(patientNew.getCity());
        patientUpdated.setCountry(patientNew.getCountry());
        patientUpdated.setIdNumber(patientNew.getIdNumber());
        patientUpdated.setBirthDate(patientNew.getBirthDate());
        patientUpdated.setZip(patientNew.getZip());

        patientRepository.save(patientUpdated);

        return true;
    }

    @Override
    public Patient getPatientByUser(User user) {
        Optional<Patient> patient = patientRepository.findByUser(user);
        return patient.get();
    }

    @Override
    public List<PatientDTO> getPatientByPage(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Patient> patients = patientRepository.findAll(pageable);
        return patients.stream().map(this::convertToPatientDTO).collect(Collectors.toList());
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).get();
    }
}
