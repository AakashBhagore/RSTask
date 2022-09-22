package com.rewards.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException() {
    }

    public TransactionNotFoundException(String msg) {
        super(msg);
    }
}
