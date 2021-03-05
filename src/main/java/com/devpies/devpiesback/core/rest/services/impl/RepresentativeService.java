package com.devpies.devpiesback.core.rest.services.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.model.roles.Representative;
import com.devpies.devpiesback.auth.application.service.interfaces.UserCrudService;
import com.devpies.devpiesback.core.application.domain.dto.RepresentativeDTO;
import com.devpies.devpiesback.core.application.domain.repository.RepresentativeRepository;
import com.devpies.devpiesback.core.rest.services.interfaces.IRepresentativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RepresentativeService implements IRepresentativeService {
    @Autowired
    RepresentativeRepository representativeRepository;

    @Autowired
    UserCrudService userCrudService;

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

    @Override
    public Boolean deleteRepresentative(User user) {
        Optional<User> fUser = userCrudService.find(user.getId());
        if(!fUser.isPresent())
            return false;
        Optional<Representative> representativeOptional = representativeRepository.findByUser(fUser.get());
        if(!representativeOptional.isPresent())
            return false;

        Representative representative = representativeOptional.get();
        representativeRepository.delete(representative);
        return true;
    }

    @Override
    public Boolean editRepresentative(User user, Representative representative) {
        Optional<User> fUser = userCrudService.find(user.getId());
        if(!fUser.isPresent())
            return false;
        Optional<Representative> representativeOptional = representativeRepository.findByUser(fUser.get());
        if(!representativeOptional.isPresent())
            return false;

        Representative representativeUpdated = representativeOptional.get();
        representativeUpdated.setHomephone(representative.getHomephone());
        representativeUpdated.setName(representative.getName());
        representativeUpdated.setSurname(representative.getSurname());
        representativeUpdated.setPhone(representative.getPhone());

        representativeRepository.save(representativeUpdated);

        return true;
    }

    @Override
    public Representative getRepresentativeByUser(User user) {
        Optional<Representative> representative = representativeRepository.findByUser(user);
        return representative.get();
    }

    @Override
    public List<RepresentativeDTO> getRepresentativesByPage(Integer page) {
        Pageable pageable = (Pageable) PageRequest.of(page, 10);
        Page<Representative> representatives =  representativeRepository.findAll(pageable);
        return representatives.stream().map(this::convertToRepresentativeDTO).collect(Collectors.toList());
    }
}


