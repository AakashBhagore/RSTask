package com.rewards.service.impl;

import com.rewards.dto.UserDTO;
import com.rewards.entity.User;
import com.rewards.exception.UserNotFoundException;
import com.rewards.repository.UserRepository;
import com.rewards.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO saveUpdateUser(UserDTO userDTO) {
        log.info("saveUpdateUser method started User name :: "+ userDTO.getName());
        User user = modelMapper.map(userDTO, User.class);
          User savedUser = userRepository.saveAndFlush(user);
          UserDTO savedUserDTO = modelMapper.map(savedUser, UserDTO.class);
        log.info("User Saved Successfully userId :: "+ savedUser.getUid());
        return savedUserDTO;
    }

    @Override
    @Cacheable(value = "users")
    public List<UserDTO> getUsers() {
        log.info("getUsers method started.");
        List<User> users = (List<User>) userRepository.findAll();
        List<UserDTO> userDTO = users.stream().map(user -> modelMapper.map(user, UserDTO.class)).collect(Collectors.toList());
        log.info("getUsers method completed total Users :: "+ users.size());
        return userDTO;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void removeUserDetailsById(Long id) {
        log.info("removeUserDetailsById method started transaction id ::"+id);
        userRepository.deleteById(id);
        log.info("removeUserDetailsById method ended");
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserDTO getUserDetailsById(Long id) {
        log.info("getUserDetailsById:: method started transaction id ::"+id);
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty()){
            log.error("getUserDetailsById:: User not found with id ::"+id);
            throw new UserNotFoundException("getUserDetailsById:: User not found with id ::"+id);
        }
        User user = userOptional.get();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        log.info("getUserDetailsById:: method ended");
        return userDTO;
    }


}
