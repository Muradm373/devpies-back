package com.devpies.devpiesback.auth.application.service.impl;

import com.devpies.devpiesback.auth.application.domain.model.User;
import com.devpies.devpiesback.auth.application.domain.repository.UserRepository;
import com.devpies.devpiesback.auth.application.service.interfaces.UserCrudService;
import com.devpies.devpiesback.core.application.domain.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserCrudService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> find(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserDTO> getAllUsers(){
        return userRepository
                .findAll()
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllUsersByPage(Integer page){
        Pageable pageable = (Pageable) PageRequest.of(page, 10);
        return userRepository
                .findAll(pageable)
                .stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToUserDTO(User user){
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        return userDTO;
    }

    @Override
    public Boolean deleteUser(User user) {
        userRepository.delete(user);
        return true;
    }
}
