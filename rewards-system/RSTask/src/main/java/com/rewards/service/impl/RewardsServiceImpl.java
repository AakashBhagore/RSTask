package com.rewards.service.impl;

import com.rewards.dto.UserDTO;
import com.rewards.dto.RewardsDto;
import com.rewards.dto.TransactionDTO;
import com.rewards.service.UserService;
import com.rewards.service.RewardsService;
import com.rewards.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class RewardsServiceImpl implements RewardsService {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;

    @Override
    public RewardsDto getRewardsMonthWise(Long id) {
        log.info("getRewardsMonthWise started customer id ::"+id);
        RewardsDto rewardsDto = null;
        HashMap<String,Long> map= new HashMap<>();
        UserDTO userDTO = userService.getUserDetailsById(id);
        rewardsDto = new RewardsDto();
        rewardsDto.setCustomerName(userDTO.getName());
        rewardsDto.setTotalRewardsPoints(userDTO.getTotalRewardPoints());
        for(Month month:Month.values()) {
            List<TransactionDTO> transactionDTOS = userDTO.getTransactions();
            Long monthPoint = totalRewardsPointInMonth(transactionService.getTransactionsByCustomerAndMonths(userDTO,month));
            if(monthPoint>0){
                map.put(month.name(),monthPoint);
            }
        }
        rewardsDto.setMonths(map);
        log.info("getRewardsMonthWise ended");
        return rewardsDto;
    }

    public Long totalRewardsPointInMonth(List<TransactionDTO> transactionDTOS){
        log.info("totalRewardsPointInMonth method started total transactions :: "+transactionDTOS.size());
        Long totalReward = 0l;
        for(TransactionDTO transactionDTO : transactionDTOS){
            totalReward = totalReward + transactionDTO.getRewardPoints();
        }
        log.info("totalRewardsPointInMonth method ended total Reward :: "+totalReward);
        return totalReward;
    }
}
