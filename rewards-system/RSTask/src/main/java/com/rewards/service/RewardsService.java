package com.rewards.service;

import com.rewards.dto.RewardsDto;
import com.rewards.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


public interface RewardsService {

    RewardsDto getRewardsMonthWise(Long id);

    Long totalRewardsPointInMonth(List<TransactionDTO> transactionDTOS);

    public static Long calculateRewardsPoint(Long amount){

        Long rewardsPoint = 0l;
        amount = amount-50;
        if(amount>0){
            if(amount/50<1) {
                rewardsPoint = amount;
            }else{
                rewardsPoint = 50 + ((amount-50)*2);
            }
        }
        return rewardsPoint;
    }

}
