package com.rewards.dto;

import lombok.Data;

import java.util.Map;

@Data
public class RewardsDto {
    private String customerName;
    private Long totalRewardsPoints;
    private Map<String,Long> months;

}
