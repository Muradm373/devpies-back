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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

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
                .findAllByRepresentative(representative, (Pageable)PageRequest.of(0, Integer.MAX_VALUE))
                .get()
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

    @Override
    public Hospital setHospitalById(Long id, Hospital hospital){
        Hospital hospitalNew = hospitalRepository.getOne(hospital.getId());
        hospitalNew.setName(hospital.getName());
        hospitalNew.setAddress(hospital.getAddress());
        hospitalNew.setWebsite(hospital.getWebsite());
        hospitalNew.setOpenTime(hospital.getOpenTime());
        hospitalNew.setCloseTime(hospital.getCloseTime());
        hospitalNew.setRepresentative(hospital.getRepresentative());
        hospitalNew.setLat(hospital.getLat());
        hospitalNew.setLng(hospital.getLng());
        hospitalRepository.save(hospitalNew);
        return (hospitalNew);
    }

    private HospitalDTO convertToHospitalDTO(Hospital hospital){
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        HospitalDTO hospitalDTO = modelMapper.map(hospital, HospitalDTO.class);

        return hospitalDTO;
    }

    @Override
    public List<HospitalDTO> getAllHospitalsByPage(Integer page) {
        Pageable pageable = (Pageable) PageRequest.of(page, 10);
        Page<Hospital> hospitals = hospitalRepository.findAll(pageable);

        return hospitals.stream().map(this::convertToHospitalDTO).collect(Collectors.toList());
    }

    @Override
    public List<HospitalDTO> getAllHospitalsByPageAndRepresentative(Integer page, User user) {

        Representative representative = representativeRepository.findByUser(user).get();
        Pageable pageable = (Pageable) PageRequest.of(page, 10);
        Page<Hospital> hospitals = hospitalRepository.findAllByRepresentative(representative, pageable);

        return hospitals.stream().map(this::convertToHospitalDTO).collect(Collectors.toList());
    }
}
