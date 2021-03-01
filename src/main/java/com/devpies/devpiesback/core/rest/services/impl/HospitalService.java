package com.devpies.devpiesback.core.rest.services.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.core.application.domain.dto.HospitalDTO;
import com.devpies.devpiesback.core.application.domain.model.Hospital;
import com.devpies.devpiesback.core.application.domain.repository.HospitalRepository;
import com.devpies.devpiesback.core.application.domain.repository.RepresentativeRepository;
import com.devpies.devpiesback.core.rest.services.interfaces.IHospitalService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HospitalService implements IHospitalService {
    @Autowired
    HospitalRepository hospitalRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RepresentativeRepository representativeRepository;

    @Override
    public List<HospitalDTO> getAllHospitalsDTO(Representative representative){
        return (hospitalRepository
                .findAllByRepresentative(representative)
                .get()
                .stream()
                .map(this::convertToHospitalDTO).collect(Collectors.toList()));
    }

    @Override
    public List<HospitalDTO> getAllHospitalsDTO() {
        return (hospitalRepository
                .findAll()
                .stream()
                .map(this::convertToHospitalDTO).collect(Collectors.toList()));
    }

    @Override
    public HospitalDTO getHospitalDTO(Long id){
        return (hospitalRepository.findById(id).map(this::convertToHospitalDTO).get());
    }
    @Override
    public Hospital getHospitalByIdAndRepresentative(Long id, User user){
        Representative representative = representativeRepository.findByUser(user).get();
        return (hospitalRepository.findByIdAndRepresentative(id, representative).get());
    }
    @Override
    public HospitalDTO updateHospitalByRepresentativeAndId(Long id, Hospital hospital, User user){
        Representative representative = representativeRepository.findByUser(user).get();
        Optional<Hospital> hospitalExisting = hospitalRepository.findByIdAndRepresentative(id, representative);

        if(hospitalExisting.isPresent()){
            Hospital hospitalUpdated = hospitalExisting.get();
            hospitalUpdated.setCloseTime(hospital.getCloseTime());
            hospitalUpdated.setOpenTime(hospital.getOpenTime());
            hospitalUpdated.setPhoneNumber(hospital.getPhoneNumber());
            hospitalUpdated.setWebsite(hospital.getWebsite());
            hospitalUpdated.setAddress(hospital.getAddress());
            hospitalUpdated.setName(hospital.getName());

            hospitalRepository.save(hospitalUpdated);
            return convertToHospitalDTO(hospitalUpdated);
        }else{
            return null;
        }
    }
    @Override
    public Boolean deleteHospitalByIdAndRepresentative(Long id, User user){
        Representative representative = representativeRepository.findByUser(user).get();
        Optional<Hospital> hospital = hospitalRepository.findByIdAndRepresentative(id, representative);
        if(hospital.isPresent()){
            hospitalRepository.delete(hospital.get());
            return true;
        }else{
            return false;
        }
    }
    @Override
    public Boolean deleteHospitalById(Long id){
        Optional<Hospital> hospital = hospitalRepository.findById(id);
        if(hospital.isPresent()){
            hospitalRepository.delete(hospital.get());
            return true;
        }else{
            return false;
        }
    }
    @Override
    public HospitalDTO updateHospitalById(Long id, Hospital hospital){
        Optional<Hospital> hospitalExisting = hospitalRepository.findById(id);

        if(hospitalExisting.isPresent()){
            Hospital hospitalUpdated = hospitalExisting.get();
            hospitalUpdated.setCloseTime(hospital.getCloseTime());
            hospitalUpdated.setOpenTime(hospital.getOpenTime());
            hospitalUpdated.setPhoneNumber(hospital.getPhoneNumber());
            hospitalUpdated.setWebsite(hospital.getWebsite());
            hospitalUpdated.setAddress(hospital.getAddress());
            hospitalUpdated.setName(hospital.getName());

            hospitalRepository.save(hospitalUpdated);
            return convertToHospitalDTO(hospitalUpdated);
        }else{
            return null;
        }
    }
    @Override
    public Hospital getHospitalById(Long id){
        return (hospitalRepository.findById(id).get());
    }

    private HospitalDTO convertToHospitalDTO(Hospital hospital){
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        HospitalDTO hospitalDTO = modelMapper.map(hospital, HospitalDTO.class);

        return hospitalDTO;
    }
}
