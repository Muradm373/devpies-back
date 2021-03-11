package com.devpies.devpiesback.core.rest.services.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Doctor;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.auth.application.domain.repository.RoleRepository;
import com.devpies.devpiesback.auth.application.service.interfaces.UserCrudService;
import com.devpies.devpiesback.common.config.Roles;
import com.devpies.devpiesback.core.application.domain.dto.DoctorDTO;
import com.devpies.devpiesback.core.application.domain.model.Hospital;
import com.devpies.devpiesback.core.application.domain.repository.DoctorRepository;
import com.devpies.devpiesback.core.application.domain.repository.HospitalRepository;
import com.devpies.devpiesback.core.application.domain.repository.RepresentativeRepository;
import com.devpies.devpiesback.core.rest.services.interfaces.IDoctorService;
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
public class DoctorService implements IDoctorService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserCrudService users;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    HospitalRepository hospitalRepository;
    @Autowired
    UserCrudService userCrudService;
    @Autowired
    RepresentativeRepository representativeRepository;

    @Override
    public List<DoctorDTO> getAllDoctorsDTO(){
        return (doctorRepository
                .findAll()
                .stream()
                .map(this::convertToDoctorDTO).collect(Collectors.toList()));
    }
    @Override
    public DoctorDTO addDoctorToHospital(Doctor doctor, Hospital hospital, String username, String password){
        User user = new User(username, username, password, roleRepository.findByName(Roles.DOCTOR.name()));
        User savedUser = users.save(user);
        doctor.setHospital(hospital);
        Doctor savedDoctor = doctorRepository.save(new Doctor(doctor, savedUser));

        hospital.getDoctors().add(savedDoctor);
        hospitalRepository.save(hospital);

        return convertToDoctorDTO(savedDoctor);
    }

    @Override
    public Doctor getDoctorByUser(User user) {
        return doctorRepository.findByUser(user).get();
    }


    @Override
    public Doctor getDoctorByIdAndRepresentative(Long id, Representative representative) {
        Doctor doctor = doctorRepository.findByRepresentativeAndId(representative, id);
        return doctor;
    }

    @Override
    public DoctorDTO getDoctorDTOById(Long id) {
        return convertToDoctorDTO(doctorRepository.findById(id).get());
    }

    @Override
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).get();
    }

    @Override
    public List<DoctorDTO> getDoctorsByHospital(Hospital hospital) {
        Pageable pageable = (Pageable) PageRequest.of(0, Integer.MAX_VALUE);
        return doctorRepository.findAllByHospital(hospital, pageable).stream().map(this::convertToDoctorDTO).collect(Collectors.toList());
    }

    @Override
    public List<DoctorDTO> getDoctorsByRepresentative(Representative representative) {
        return doctorRepository.findAllByRepresentative(representative).stream().map(this::convertToDoctorDTO).collect(Collectors.toList());
    }

    @Override
    public DoctorDTO editDoctorByIdAndRepresentative(Long id, Representative representative, Doctor updatedDoctor) {
        Doctor doctor = doctorRepository.findByRepresentativeAndId(representative, id);
        if(doctor == null)
            return null;
        doctor.setHomephone(updatedDoctor.getHomephone());
        doctor.setPhone(updatedDoctor.getPhone());
        doctor.setName(updatedDoctor.getName());
        doctor.setSurname(updatedDoctor.getSurname());

        doctorRepository.save(doctor);
        return null;
    }

    @Override
    public DoctorDTO editDoctorById(Long id, Doctor updatedDoctor) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(id);
        if(doctorOpt.isPresent())
            return null;
        Doctor doctor = doctorOpt.get();
        doctor.setHomephone(updatedDoctor.getHomephone());
        doctor.setPhone(updatedDoctor.getPhone());
        doctor.setName(updatedDoctor.getName());
        doctor.setSurname(updatedDoctor.getSurname());

        DoctorDTO doctorDTO = convertToDoctorDTO(doctorRepository.save(doctor));
        return doctorDTO;
    }

    @Override
    public Boolean deleteDoctorByIdAndRepresentative(Long id, Representative representative) {
        Doctor doctor = doctorRepository.findByRepresentativeAndId(representative, id);
        if(doctor == null)
            return false;
        doctorRepository.delete(doctor);
        return true;
    }

    @Override
    public Boolean deleteDoctorById(Long id) {
        return null;
    }

    private DoctorDTO convertToDoctorDTO(Doctor doctor){
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        DoctorDTO doctorDTO = modelMapper.map(doctor, DoctorDTO.class);

        return doctorDTO;
    }

    @Override
    public Boolean deleteDoctor(User user) {
        Optional<User> fUser = userCrudService.find(user.getId());
        if(!fUser.isPresent())
            return false;
        Optional<Doctor> doctorOptional = doctorRepository.findByUser(fUser.get());
        if(!doctorOptional.isPresent())
            return false;

        Doctor doctor = doctorOptional.get();
        doctorRepository.delete(doctor);
        return true;
    }

    @Override
    public Boolean editDoctor(User user, Doctor updatedDoctor) {
        Optional<User> fUser = userCrudService.find(user.getId());
        if(!fUser.isPresent())
            return false;
        Optional<Doctor> doctorOpt = doctorRepository.findByUser(fUser.get());
        if(!doctorOpt.isPresent())
            return false;

        Doctor doctor = doctorOpt.get();
        doctor.setHomephone(updatedDoctor.getHomephone());
        doctor.setPhone(updatedDoctor.getPhone());
        doctor.setName(updatedDoctor.getName());
        doctor.setSurname(updatedDoctor.getSurname());

        doctorRepository.save(doctor);

        return true;
    }

    @Override
    public List<DoctorDTO> getAllDoctorsByPage(Integer page) {
        Pageable pageable = (Pageable) PageRequest.of(page, 10);
        Page<Doctor> doctors =  doctorRepository.findAll(pageable);

        return doctors.stream().map(this::convertToDoctorDTO).collect(Collectors.toList());
    }

    @Override
    public List<DoctorDTO> getAllDoctorsByPageAndHospitalId(Integer page, Long hospitalId) {
        Hospital hospital = hospitalRepository.getOne(hospitalId);
        Pageable pageable = (Pageable) PageRequest.of(page, 10);
        Page<Doctor> doctors =  doctorRepository.findAllByHospital(hospital, pageable);

        return doctors.stream().map(this::convertToDoctorDTO).collect(Collectors.toList());
    }

    @Override
    public List<DoctorDTO> getAllDoctorsByPageAndRepresentative(Integer page, User user) {
        Representative representative = representativeRepository.findByUser(user).get();
        Pageable pageable = (Pageable) PageRequest.of(page, 10);
        List<Doctor> doctors =  doctorRepository.findAllByRepresentativePageable(representative);

        return doctors.stream().map(this::convertToDoctorDTO).collect(Collectors.toList());
    }
}
