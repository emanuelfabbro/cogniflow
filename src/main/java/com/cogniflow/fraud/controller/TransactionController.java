package com.cogniflow.fraud.controller;

import com.cogniflow.fraud.dto.TransactionRequest;
import com.cogniflow.fraud.kafka.producer.TransactionProducer;
import com.cogniflow.fraud.model.Transaction;
import com.cogniflow.fraud.model.TransactionStatus;
import com.cogniflow.fraud.repository.TransactionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Endpoints for processing financial transactions")
public class TransactionController {

    private final TransactionProducer transactionProducer;
    private final TransactionRepository transactionRepository; // Necesitarás crear esta interfaz
    private final ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Process a new transaction", description = "Validates, saves as PENDING and sends to Kafka for fraud analysis")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionRequest request) {

        // 1. Convertir DTO a Entidad
        Transaction transaction = modelMapper.map(request, Transaction.class);
        transaction.setStatus(TransactionStatus.PENDING); // Estado inicial

        // 2. Guardar en PostgreSQL (Patrón: Guardar antes de enviar eventos)
        Transaction savedTransaction = transactionRepository.save(transaction);

        // 3. Enviar evento a Kafka (Async)
        // Enviamos el ID y el Monto para que el consumidor lo analice
        String eventMessage = String.format("{\"id\":\"%s\", \"amount\":%s}",
                savedTransaction.getId(),
                savedTransaction.getAmount());

        transactionProducer.sendTransaction(eventMessage);

        return ResponseEntity.ok(savedTransaction);
    }
}