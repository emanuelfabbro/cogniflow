package com.cogniflow.fraud.controller;

import com.cogniflow.fraud.dto.TransactionRequest;
import com.cogniflow.fraud.model.Transaction;
import com.cogniflow.fraud.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions")
public class TransactionController {

    // Ahora inyectamos el Servicio, NO el Repo ni el Producer
    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Process a new transaction")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionRequest request) {
        // Una sola línea: Delegar al servicio
        Transaction createdTransaction = transactionService.createTransaction(request);
        return ResponseEntity.ok(createdTransaction);
    }
}