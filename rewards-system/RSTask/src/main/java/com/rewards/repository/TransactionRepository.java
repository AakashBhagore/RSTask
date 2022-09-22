package com.rewards.repository;

import com.rewards.entity.User;
import com.rewards.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction,Long> {

    List<Transaction> findAllByUserAndDateBetween(User user, LocalDate startDate,
                                                      LocalDate endDate);
}
