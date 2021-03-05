package com.devpies.devpiesback.core.rest.services.interfaces;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.core.application.domain.dto.HospitalDTO;
import com.devpies.devpiesback.core.application.domain.model.Hospital;

import java.awt.print.Pageable;
import java.util.List;

public interface IHospitalService {
    public List<HospitalDTO> getAllHospitalsDTO(Representative representative);
    public List<HospitalDTO> getAllHospitalsDTO();
    public List<HospitalDTO> getAllHospitalsByPage(Integer page);
    public HospitalDTO getHospitalDTO(Long id);
    public Hospital getHospitalByIdAndRepresentative(Long id, User user);
    public HospitalDTO updateHospitalByRepresentativeAndId(Long id, Hospital hospital, User user);
    public Boolean deleteHospitalByIdAndRepresentative(Long id, User user);
    public Boolean deleteHospitalById(Long id);
    public HospitalDTO updateHospitalById(Long id, Hospital hospital);
    public Hospital getHospitalById(Long id);

}
