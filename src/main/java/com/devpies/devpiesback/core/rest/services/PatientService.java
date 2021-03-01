package com.devpies.devpiesback.core.rest.services;

import com.devpies.devpiesback.auth.application.domain.model.roles.Patient;
import com.devpies.devpiesback.core.application.domain.dto.PatientDTO;
import com.devpies.devpiesback.core.application.domain.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class PatientService {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<PatientDTO> getAllPatientsDTO(){
        return (patientRepository
                .findAll()
                .stream()
                .map(this::convertToPatientDTO).collect(Collectors.toList()));
    }

    private PatientDTO convertToPatientDTO(Patient patient){
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        PatientDTO patientDTO = modelMapper.map(patient, PatientDTO.class);

        return patientDTO;
    }
}
