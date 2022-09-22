package com.rewards.service;

import com.rewards.dto.UserDTO;
import java.util.List;

public interface UserService {
    UserDTO saveUpdateUser(UserDTO userDTO);

    List<UserDTO> getUsers();

    void removeUserDetailsById(Long id);

    UserDTO getUserDetailsById(Long id);


}
