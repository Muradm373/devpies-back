package com.devpies.devpiesback.core.rest.services.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.core.application.domain.dto.RepresentativeDTO;
import com.devpies.devpiesback.core.application.domain.repository.RepresentativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepresentativeService {
    @Autowired
    RepresentativeRepository representativeRepository;

    public List<RepresentativeDTO> getAllRepresentativesDTO(){
        return (representativeRepository
        .findAll()
        .stream()
        .map(this::convertToRepresentativeDTO).collect(Collectors.toList()));
    }
    public Representative findByUser(User user){
        return representativeRepository.findByUser(user).get();
    }

    private RepresentativeDTO convertToRepresentativeDTO(Representative representative){
        RepresentativeDTO representativeDTO = new RepresentativeDTO();
        representativeDTO.setEmail(representative.getUser().getEmail());
        representativeDTO.setId(representative.getId());
        representativeDTO.setName(representative.getName());
        representativeDTO.setSurname(representative.getSurname());
        representativeDTO.setPhone(representative.getPhone());
        representativeDTO.setPictureUrl(null);

        return representativeDTO;
    }
}


