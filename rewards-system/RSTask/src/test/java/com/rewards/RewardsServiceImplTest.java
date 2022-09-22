package com.rewards;

import com.rewards.dto.UserDTO;
import com.rewards.dto.RewardsDto;
import com.rewards.dto.TransactionDTO;
import com.rewards.service.RewardsService;
import com.rewards.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rewards.service.UserService;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Slf4j
public class RewardsServiceImplTest {

	 @Autowired private UserService userService;

	 @Autowired private TransactionService transactionService;

	 @Autowired private RewardsService rewardsService;
	 
	 @Test
	 public void getRewardPointsMonthWise() {
		 TransactionDTO transactionDto = new TransactionDTO();
		 UserDTO userDto = new UserDTO();
		 userDto.setName("test1");
		 UserDTO userDTO = userService.saveUpdateUser(userDto);
		 transactionDto.setAmount(170l); transactionDto.setDate(LocalDate.of(2022, 01, 025));
		 transactionDto.setUserDTO(userDTO);
		 TransactionDTO savedTransactionDTO = transactionService.saveUpdateTransaction(transactionDto);
		 RewardsDto rewardsDto = rewardsService.getRewardsMonthWise(userDTO.getUid());
		 Assertions.assertNotNull(rewardsDto);
		 Assertions.assertTrue(rewardsDto.getMonths().size()>0);
		 Assertions.assertNotNull(rewardsDto.getMonths());
	 }

	 @Test
	public void calculateRewardsPointTest(){
		Assertions.assertEquals(90,RewardsService.calculateRewardsPoint(120l));
		 Assertions.assertEquals(20,RewardsService.calculateRewardsPoint(70l));
		 Assertions.assertEquals(0,RewardsService.calculateRewardsPoint(45l));
	}

	@Test
	public void totalRewardsPointInMonthTest(){
		TransactionDTO transactionDto1 = new TransactionDTO();
		TransactionDTO transactionDto2 = new TransactionDTO();
		UserDTO userDto = new UserDTO();
		userDto.setName("test1");
		UserDTO userDTO = userService.saveUpdateUser(userDto);
		transactionDto1.setAmount(120l); transactionDto1.setDate(LocalDate.of(2022, 01, 025));
		transactionDto1.setUserDTO(userDTO);
		TransactionDTO savedTransactionDTO1 = transactionService.saveUpdateTransaction(transactionDto1);
		transactionDto2.setAmount(70l); transactionDto1.setDate(LocalDate.of(2022, 02, 025));
		transactionDto2.setUserDTO(userDTO);
		TransactionDTO savedTransactionDTO2 = transactionService.saveUpdateTransaction(transactionDto2);

		Assertions.assertEquals(110,rewardsService.totalRewardsPointInMonth(List.of(savedTransactionDTO1,savedTransactionDTO2)));
	}
}
