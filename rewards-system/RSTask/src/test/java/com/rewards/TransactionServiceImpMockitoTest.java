package com.rewards;

import com.rewards.dto.TransactionDTO;
import com.rewards.dto.UserDTO;
import com.rewards.entity.Transaction;
import com.rewards.entity.User;
import com.rewards.exception.TransactionNotFoundException;
import com.rewards.exception.UserNotFoundException;
import com.rewards.repository.TransactionRepository;
import com.rewards.repository.UserRepository;
import com.rewards.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImpMockitoTest {

  @InjectMocks
  private TransactionServiceImpl transactionServiceImpl;

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ModelMapper modelMapper;

  private Transaction transaction;
  private List<Transaction> transactionList;
  private TransactionDTO transactionDto;
  private List<TransactionDTO> transactionDTOList;
  private User user;
  private UserDTO userDto;

  @BeforeEach
  public void setUP() throws Exception {
    MockitoAnnotations.initMocks(this);

    transactionList = new ArrayList<Transaction>();
    transaction = new Transaction();
    transaction.setTid(4L); transaction.setAmount(170l); transaction.setDate(LocalDate.of(2022, 01, 025)); transactionList.add(transaction);

    user = new User();
    user.setUid(1L); user.setName("test2"); user.setTotalRewardPoints(190L); user.setTransactions(transactionList);
    transaction.setUser(user);

    transactionDTOList = new ArrayList<TransactionDTO>();
    transactionDto = new TransactionDTO();
    transactionDto.setDate(transaction.getDate()); transactionDto.setAmount(transaction.getAmount());
    transactionDTOList.add(transactionDto);

    userDto = new UserDTO();
    userDto.setUid(user.getUid()); userDto.setName("test2"); userDto.setTotalRewardPoints(190L);
    transactionDto.setUserDTO(userDto);


//    UserDTO userDto = modelMapper.map(user, UserDTO.class);
//    TransactionDTO transactionDto = modelMapper.map(transaction,TransactionDTO.class);
//    transactionDto.setUserDTO(userDto);
//    BeanUtils.copyProperties(userDto, user);
  }


  @Test
  public void saveUpdateTransaction_Success() {

    when(userRepository.findById(user.getUid())).thenReturn(Optional.ofNullable(user));
    when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

    TransactionDTO savedTransactionDTO = transactionServiceImpl.saveUpdateTransaction(transactionDto);

    Assertions.assertNotNull(savedTransactionDTO);
    Assertions.assertNotNull(savedTransactionDTO.getTid());
    Assertions.assertSame(transactionDto.getDate(), savedTransactionDTO.getDate());
    Assertions.assertEquals(transactionDto.getAmount(),savedTransactionDTO.getAmount());
    Assertions.assertTrue(savedTransactionDTO.getDate().equals(transaction.getDate()));
  }

  @Test
  public void saveUpdateTransaction_Failure() {

    when(userRepository.findById(userDto.getUid())).thenReturn(Optional.ofNullable(user));
    when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

    transactionDto.getUserDTO().setUid(0L);
    Assertions.assertThrows(PotentialStubbingProblem.class,()-> transactionServiceImpl.saveUpdateTransaction(transactionDto));
    transactionDto.setUserDTO(null);
    Assertions.assertThrows(UserNotFoundException.class,()->transactionServiceImpl.saveUpdateTransaction(transactionDto));
    TransactionDTO transactionDTO = new TransactionDTO();
    Assertions.assertThrows(UserNotFoundException.class, ()-> transactionServiceImpl.saveUpdateTransaction(transactionDTO));
  }

  @Test
  public void getTransactions_Success() {

    when(transactionRepository.findAll()).thenReturn(transactionList);

    List<TransactionDTO> transactionDtoList = transactionServiceImpl.getTransactions();

    Assertions.assertTrue(!transactionDtoList.isEmpty());
    Assertions.assertNotNull(transactionDtoList);
    Assertions.assertEquals(transactionDtoList.size(),transactionDTOList.size());
    Assertions.assertNotNull(transactionDtoList.listIterator(0));
  }

  @Test
  public void getTransactions_Failure() {
    List<Transaction> transactionList1 = new ArrayList<Transaction>();
    when(transactionRepository.findAll()).thenReturn(transactionList1);
    Assertions.assertThrows(TransactionNotFoundException.class, ()-> transactionServiceImpl.getTransactions());
  }


  @Test
  public void removeTransaction_Success() {
    Transaction transaction1 = new Transaction();
    transaction.setTid(2L);
    transaction1.setTid(2L); transaction1.setDate(transaction.getDate()); transaction1.setAmount(transaction.getAmount());
    transaction1.setRewardPoints(transaction.getRewardPoints()); transaction1.setUser(transaction.getUser());

    when(transactionRepository.findById(transaction1.getTid())).thenReturn(Optional.ofNullable(transaction1));
    when(userRepository.save(transaction1.getUser())).thenReturn(user);
    transactionRepository.delete(transaction1);
    Mockito.verify(transactionRepository,times(1)).delete(transaction1);
    //  when(transactionRepository.delete(transaction1)).thenReturn(IllegalArgumentException.class);

    Assertions.assertEquals(Optional.empty(),transactionRepository.findById(0L));
    Assertions.assertThrows(PotentialStubbingProblem.class,()->transactionServiceImpl.removeTransaction(0L));
  }

  @Test
  public void removeTransaction_Failure() {

    when(transactionRepository.findById(transaction.getTid())).thenReturn(Optional.ofNullable(transaction));
    when(userRepository.save(transaction.getUser())).thenReturn(user);
    transactionRepository.delete(transaction);
    Mockito.verify(transactionRepository,times(1)).delete(transaction);

    Assertions.assertThrows(PotentialStubbingProblem.class,()-> transactionServiceImpl.removeTransaction(0L));
    Assertions.assertThrows(NullPointerException.class,()->transactionServiceImpl.removeTransaction(transaction.getTid()));
    Assertions.assertThrows(NullPointerException.class,()->transactionServiceImpl.removeTransaction(4L));
  }


  @Test
  public void getTransaction_Success() {

    when(transactionRepository.findById(transaction.getTid())).thenReturn(Optional.ofNullable(transaction));
    TransactionDTO transactionDTOTemp = transactionServiceImpl.getTransaction(transaction.getTid());
    transactionDto.setTid(transaction.getTid());

    Assertions.assertNotNull(transactionDTOTemp.getTid());
    Assertions.assertNotNull(transactionDTOTemp.getTid(), String.valueOf(transactionDto.getTid()));
    Assertions.assertNotEquals(transactionDTOTemp,transactionDto);
    Assertions.assertTrue(transactionDTOTemp!=null);
    Assertions.assertSame(transactionDTOTemp.getDate(),transactionDto.getDate());
  }

  @Test
  public void getTransaction_Failure() {
    when(transactionRepository.findById(transaction.getTid())).thenReturn(Optional.ofNullable(transaction));
    TransactionDTO transactionDTOTemp = transactionServiceImpl.getTransaction(transaction.getTid());

    Assertions.assertThrows(TransactionNotFoundException.class,()->transactionServiceImpl.getTransaction(0L));
    Assertions.assertThrows(TransactionNotFoundException.class,()->transactionServiceImpl.getTransaction(null));
  }
}
