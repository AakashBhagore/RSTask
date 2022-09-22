package com.rewards.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;


@Entity
@Getter
@Setter
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uid;

    private String name;

    @OneToMany(targetEntity = Transaction.class,fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID",referencedColumnName = "UID")
    private List<Transaction> transactions;

    private Long totalRewardPoints = 0l;

}
