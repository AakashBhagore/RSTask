package com.rewards.service.impl;

import com.rewards.dto.UserDTO;
import com.rewards.dto.TransactionDTO;
import com.rewards.entity.User;
import com.rewards.entity.Transaction;
import com.rewards.exception.UserNotFoundException;
import com.rewards.exception.TransactionNotFoundException;
import com.rewards.repository.UserRepository;
import com.rewards.repository.TransactionRepository;
import com.rewards.service.RewardsService;
import com.rewards.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TransactionDTO saveUpdateTransaction(TransactionDTO transactionDTO){
        log.info("saveUpdateTransaction method started Purchase value :: "+transactionDTO.getAmount());

        UserDTO userDTO = transactionDTO.getUserDTO();
        if(userDTO ==null){
            throw new UserNotFoundException("Without Customer Transaction would not Happen");
        }
        Optional<User> existCustomerOptional = userRepository.findById(userDTO.getUid());
        if(existCustomerOptional.isEmpty()){
            throw new UserNotFoundException("Customer Not Exist");
        }
        transactionDTO.setRewardPoints(RewardsService.calculateRewardsPoint(transactionDTO.getAmount()));
        User existUser = existCustomerOptional.get();
        existUser.setTotalRewardPoints(existUser.getTotalRewardPoints()+transactionDTO.getRewardPoints());
        UserDTO existUserDto = modelMapper.map(existUser, UserDTO.class);
        transactionDTO.setUserDTO(existUserDto);
        Transaction transaction= modelMapper.map(transactionDTO,Transaction.class);
        Transaction savedTransaction = transactionRepository.save(transaction);
        TransactionDTO savedTransactionDto= modelMapper.map(savedTransaction,TransactionDTO.class);
        log.info("Transaction Saved Successfully transactionId :: "+savedTransaction.getTid());
        return savedTransactionDto;
    }

    @Override
    public List<TransactionDTO> getTransactions() {
        log.info("getTransactions method started.");
        List<Transaction> transactions = (List<Transaction>) transactionRepository.findAll();
        List<TransactionDTO> transactionDTOS = transactions.stream().map(user -> modelMapper.map(user, TransactionDTO.class)).collect(Collectors.toList());
        log.info("getTransactions method completed total tranactions :: "+transactions.size());
        return transactionDTOS;
    }

    @Override
    public void removeTransaction(Long id) {
        log.info("removeTransaction method started transaction id ::"+id);
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if(transactionOptional.isEmpty()){
            throw new TransactionNotFoundException("Transaction not found with id ::"+id);
        }
        Transaction transaction = transactionOptional.get();
        User user = transaction.getUser();

        user.setTotalRewardPoints(user.getTotalRewardPoints()-transaction.getRewardPoints());
        user.getTransactions().remove(transaction);

        userRepository.save(user);
                transactionRepository.delete(transaction);
        log.info("removeTransaction method ended");
    }

    @Override
    public TransactionDTO getTransaction(Long id) {
        log.info("getTransaction method started transaction id ::"+id);
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if(transactionOptional.isEmpty()){
            log.error("getTransaction Transaction Not Found with id ::"+id);
            throw new TransactionNotFoundException("Invalid Transaction ID ::"+id);
        }
        TransactionDTO transactionDTO = modelMapper.map(transactionOptional.get(),TransactionDTO.class);
        log.info("getTransaction method ended");
        return transactionDTO;
    }

    @Override
    public List<TransactionDTO> getTransactionsByCustomerAndMonths(UserDTO userDTO, Month month) {
        log.info("getTransactionsByMonths method started MONTH ::"+month);
        LocalDate startDate = LocalDate.of(2022, month,1);
        LocalDate endDate = LocalDate.of(2022,month, startDate.withDayOfMonth(
                startDate.getMonth().length(startDate.isLeapYear())).getDayOfMonth());
        User user = modelMapper.map(userDTO, User.class);
        List<Transaction> transactions = (List<Transaction>) transactionRepository.findAllByUserAndDateBetween(user,startDate,endDate);
        List<TransactionDTO> transactionDTOS = transactions.stream().map(transaction -> modelMapper.map(transaction, TransactionDTO.class)).collect(Collectors.toList());
        log.info("getTransactionsByMonths method completed total transactions :: "+transactions.size());
        return transactionDTOS;

    }
}
