package com.rewards.controller;

import com.rewards.dto.TransactionDTO;
import com.rewards.reponse.ResponseHandler;
import com.rewards.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @GetMapping("transactions")
    public ResponseEntity<Object> getTransactions(){
        log.info("getTransactions started");
        try {
            List<TransactionDTO> transactionDTOS= transactionService.getTransactions();
            log.info("getTransactions total transactions ::"+transactionDTOS.size());
            return ResponseHandler.generateResponse("success", HttpStatus.OK, transactionDTOS);
        }catch (Exception e) {
            log.error("Exception occured in getTransactions ::"+ e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,null) ;
        }
    }

    @GetMapping("transactions/{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable Long id){
        log.info("getTransaction started");
        try {
            TransactionDTO transactionDTO = transactionService.getTransaction(id);
            log.info("getTransactions transaction get successfully");
            return ResponseHandler.generateResponse("success", HttpStatus.OK, transactionDTO);
        }catch (Exception e) {
            log.error("Exception occured in getTransaction ::"+ e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,id) ;
        }
    }

    @PostMapping("transactions")
    public ResponseEntity<Object> addTransaction(@RequestBody TransactionDTO transactionDTO){
        log.info("addTransaction started");
        try {
            TransactionDTO savedTransactionDTO = transactionService.saveUpdateTransaction(transactionDTO);
            log.info("addTransaction Transaction Done Successfully");
            return ResponseHandler.generateResponse("Transaction Done Successfully", HttpStatus.OK, savedTransactionDTO);
        }
     catch (Exception e) {
         log.error("Exception occured in addTransaction ::"+ e.getMessage());
        return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY,transactionDTO) ;
    }
    }

    @PutMapping("transactions")
    public ResponseEntity<Object> updateTransaction(@RequestBody TransactionDTO transactionDTO){
        log.info("updateTransaction started transaction id ::"+transactionDTO.getTid());
        try {
            TransactionDTO savedTransactionDTO = transactionService.saveUpdateTransaction(transactionDTO);
            log.info("updateTransaction transaction updated successfully on id ::"+transactionDTO.getTid());
            return ResponseHandler.generateResponse("Transaction Updated Successfully", HttpStatus.OK, savedTransactionDTO);
        }catch (Exception e) {
            log.error("Exception occured in updateTransaction ::"+ e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY,transactionDTO) ;
        }
    }

    @DeleteMapping("transactions/{id}")
    public ResponseEntity<Object> deleteTransaction(@PathVariable Long id){
        log.info("deleteTransaction started transaction id ::"+id);
        try {
            transactionService.removeTransaction(id);
            log.info("deleteTransaction transaction deleted successfully with id ::"+id);
            return ResponseHandler.generateResponse("Transaction Deleted Successfully", HttpStatus.OK, null);
        }catch (Exception e) {
            log.error("Exception occured in deleteTransaction ::"+ e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,null) ;
        }
    }

}
