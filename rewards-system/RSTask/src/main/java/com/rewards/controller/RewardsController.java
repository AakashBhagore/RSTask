package com.rewards.controller;

import com.rewards.dto.RewardsDto;
import com.rewards.reponse.ResponseHandler;
import com.rewards.service.RewardsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class  RewardsController {

    @Autowired
    private RewardsService rewardsService;

    @GetMapping("rewards/{id}")
    public ResponseEntity<Object> getRewardsDetails(@PathVariable Long id){
        log.info("getRewardsDetails started customer id ::"+id);
        try {
            RewardsDto rewardsDto = rewardsService.getRewardsMonthWise(id);
            log.info("getRewardsDetails rewards details retive successfully");
            return ResponseHandler.generateResponse("success", HttpStatus.OK, rewardsDto);
        }catch (Exception e) {
            log.error("Exception occured in getRewardsDetails ::"+ e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,null) ;
        }
    }


}
