package com.cogniflow.fraud.service.impl;

import com.cogniflow.fraud.document.TransactionDocument;
import com.cogniflow.fraud.dto.TransactionRequest;
import com.cogniflow.fraud.kafka.producer.TransactionProducer;
import com.cogniflow.fraud.model.Transaction;
import com.cogniflow.fraud.model.TransactionStatus;
import com.cogniflow.fraud.repository.TransactionRepository;
import com.cogniflow.fraud.repository.TransactionSearchRepository;
import com.cogniflow.fraud.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionSearchRepository transactionSearchRepository;
    private final TransactionProducer transactionProducer;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Transaction createTransaction(TransactionRequest request) {
        log.info("Procesando nueva transacción para cuenta: {}", request.getAccountId());

        Transaction transaction = modelMapper.map(request, Transaction.class);
        transaction.setStatus(TransactionStatus.PENDING);

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transacción guardada en Postgres con ID: {}", savedTransaction.getId());

        try {
            TransactionDocument elasticDocument = TransactionDocument.builder()
                    .id(savedTransaction.getId().toString())
                    .accountId(savedTransaction.getAccountId().toString())
                    .amount(savedTransaction.getAmount())
                    .status(savedTransaction.getStatus().name())
                    .build();

            transactionSearchRepository.save(elasticDocument);
            log.info("Transacción indexada en Elasticsearch exitosamente.");
        } catch (Exception e) {
            log.error("Error al indexar en Elasticsearch, pero la transacción de BD fue exitosa", e);
        }

        String eventMessage = String.format("{\"id\":\"%s\", \"amount\":%s}",
                savedTransaction.getId(),
                savedTransaction.getAmount());

        transactionProducer.sendTransaction(eventMessage);

        return savedTransaction;
    }
}