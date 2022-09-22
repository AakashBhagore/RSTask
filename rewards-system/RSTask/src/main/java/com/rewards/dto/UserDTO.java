package com.rewards.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UserDTO implements Serializable {


    private Long uid;

    private String name;

    private List<TransactionDTO> transactions;

    private Long totalRewardPoints = 0l;

}
