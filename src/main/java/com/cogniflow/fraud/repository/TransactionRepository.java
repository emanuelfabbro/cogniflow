package com.cogniflow.fraud.repository;

import com.cogniflow.fraud.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
}