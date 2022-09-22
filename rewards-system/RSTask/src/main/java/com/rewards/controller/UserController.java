package com.rewards.controller;

import com.rewards.dto.UserDTO;
import com.rewards.reponse.ResponseHandler;
import com.rewards.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("users")
    public ResponseEntity<Object> getUsers(){
        log.info("getUsers started");
        try {
            List<UserDTO> users = userService.getUsers();
            log.info("getUsers users list size ::"+users.size());
            return ResponseHandler.generateResponse("success", HttpStatus.OK, users);
        }catch (Exception e) {
            log.error("Exception occurred in getUsers ::"+ e.getMessage());
            return ResponseHandler.generateResponse("error",HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()) ;
        }
    }

    @GetMapping("users/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id){
        log.info("getUser started id ::"+id);
        try {
            UserDTO userDTO = userService.getUserDetailsById(id);
            log.info("getUser retrieve successfully with id ::"+id);
            return ResponseHandler.generateResponse("success", HttpStatus.OK, userDTO);
        }catch (Exception e) {
            log.error("Exception occurred in getUser ::"+ e.getMessage());
            return ResponseHandler.generateResponse("error",HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()) ;
        }
    }

    @PostMapping("users")
    public ResponseEntity<Object> addUser(@RequestBody UserDTO userDTO){
        log.info("addUser started");
        try {
            UserDTO savedUserDTO = userService.saveUpdateUser(userDTO);
            log.info("addUser:: user added successfully with id ::"+ savedUserDTO.getUid());
            return ResponseHandler.generateResponse("success", HttpStatus.OK, savedUserDTO);
        }catch (Exception e) {
            log.error("Exception occurred in addUser ::"+ e.getMessage());
            return ResponseHandler.generateResponse("error",HttpStatus.UNPROCESSABLE_ENTITY,e.getMessage()) ;
        }
    }

    @PutMapping("user")
    public ResponseEntity<Object> updateUser(@RequestBody UserDTO userDTO){
        log.info("updateUser started user id ::"+ userDTO.getUid());
        try {
            UserDTO updateUser = userService.saveUpdateUser(userDTO);
            log.info("updateUser:: user updated successfully with id ::"+updateUser.getUid());
            return ResponseHandler.generateResponse("success", HttpStatus.OK, updateUser);
        }catch (Exception e) {
            log.error("Exception occurred in updateUser ::"+ e.getMessage());
            return ResponseHandler.generateResponse("error",HttpStatus.UNPROCESSABLE_ENTITY,e.getMessage()) ;
        }
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id){
        log.info("deleteUser started user id ::"+id);
        try {
            userService.removeUserDetailsById(id);
            log.info("deleteUser:: user deleted successfully with id ::"+id);
            return ResponseHandler.generateResponse("success", HttpStatus.OK, "User Deleted Successfully");
        }catch (Exception e) {
            log.error("Exception occurred in deleteUser ::"+ e.getMessage());
            return ResponseHandler.generateResponse("error",HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()) ;
        }
    }
}
