package com.rewards;

import com.rewards.dto.UserDTO;
import com.rewards.dto.TransactionDTO;
import com.rewards.exception.UserNotFoundException;
import com.rewards.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserServiceImplTests {

    @Autowired
    private UserService userService;

    @Test
    public void getUsersSuccessTest() {
        UserDTO userDto = new UserDTO();
        userDto.setName("Test1");
        UserDTO savedUser = userService.saveUpdateUser(userDto);
        List<UserDTO> users = userService.getUsers();
        Assertions.assertTrue(users.size()>0);
    }

    @Test
    public void saveUserSuccessTest() {
        UserDTO userDto = new UserDTO();
        userDto.setName("test1");
        List<TransactionDTO> transactionList = new ArrayList<TransactionDTO>();
        transactionList.add(new TransactionDTO(120l,0l, LocalDate.of(2022, 01, 02)));
        transactionList.add(new TransactionDTO(125l,0l,LocalDate.of(2022, 01, 03)));
        userDto.setTransactions(transactionList);
        UserDTO user = userService.saveUpdateUser(userDto);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getUid());
        Assertions.assertEquals("test1",user.getName());
    }

    @Test
    public void saveUserFailerTest() {
        UserDTO userDto = new UserDTO();
        userDto.setName("test1");
        List<TransactionDTO> transactionList = new ArrayList<TransactionDTO>();
        transactionList.add(new TransactionDTO(120l,0l, LocalDate.of(2022, 01, 02)));
        transactionList.add(new TransactionDTO(125l,0l,LocalDate.of(2022, 01, 03)));
        userDto.setTransactions(transactionList);
        Assertions.assertThrows(NullPointerException.class, ()-> userService.saveUpdateUser(null)) ;
    }

    @Test
    public void updatedUserTransaction(){
        UserDTO userDto = new UserDTO();
        userDto.setName("test1");
        List<TransactionDTO> transactionList = new ArrayList<TransactionDTO>();
        transactionList.add(new TransactionDTO(120l,0l,LocalDate.of(2022, 01, 02)));
        transactionList.add(new TransactionDTO(125l,0l,LocalDate.of(2022, 01, 03)));
        userDto.setTransactions(transactionList);
        UserDTO user = userService.saveUpdateUser(userDto);
        user.setName("Test2");
        UserDTO updateUser = userService.saveUpdateUser(user);
        Assertions.assertEquals("Test2",updateUser.getName());
        Assertions.assertNotNull(updateUser);
    }


    @Test
    public void getUserByIdSuccessTest() {

        UserDTO userDto = new UserDTO();
        UserDTO usr = userService.saveUpdateUser(userDto);
        UserDTO user = userService.getUserDetailsById(usr.getUid());
        Assertions.assertEquals(usr.getUid(),user.getUid());
        Assertions.assertNotNull(user);

    }

    @Test
    public void getUserByIdFailureTest() {
        UserDTO userDto = new UserDTO();
        Assertions.assertThrows(UserNotFoundException.class, ()-> userService.getUserDetailsById(0l)) ;
    }

    @Test
    public void removeUserTest() {
        UserDTO userDto = new UserDTO();
        userDto.setName("test1");
        UserDTO user = userService.saveUpdateUser(userDto);

        userService.removeUserDetailsById(user.getUid());
        Assertions.assertThrows(UserNotFoundException.class, ()-> userService.getUserDetailsById(user.getUid())) ;
    }

}
