package com.cogniflow.fraud.service;

import com.cogniflow.fraud.dto.TransactionRequest;
import com.cogniflow.fraud.model.Transaction;

public interface TransactionService {
    Transaction createTransaction(TransactionRequest request);
}