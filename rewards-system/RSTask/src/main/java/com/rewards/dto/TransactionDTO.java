package com.rewards.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
public class TransactionDTO implements Serializable {

    private Long tid;
    private Long amount;
    private Long rewardPoints;
    private UserDTO userDTO;
    private LocalDate date;
	public TransactionDTO(Long amount, Long rewardPoints, LocalDate date) {
		super();
		this.amount = amount;
		this.rewardPoints = rewardPoints;
		this.date = date;
	}
    
    
}

