package com.cogniflow.fraud.kafka.consumer;

import com.cogniflow.fraud.model.Transaction;
import com.cogniflow.fraud.model.TransactionStatus;
import com.cogniflow.fraud.repository.TransactionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 1. Habilita Mockito
class TransactionConsumerTest {

    @Mock // Crea un simulacro del repositorio (no conecta a DB real)
    private TransactionRepository transactionRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks // Inyecta los mocks dentro de nuestra clase real
    private TransactionConsumer transactionConsumer;

    @Test
    void whenAmountIsHigh_thenDetectFraud() throws Exception {
        // GIVEN (Preparar datos)
        String jsonMessage = "{\"id\":\"d290f1ee-6c54-4b01-90e6-d701748f0851\", \"amount\":20000.00}";
        UUID txId = UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851");

        // Simulamos la transacción existente en DB
        Transaction existingTx = new Transaction();
        existingTx.setId(txId);
        existingTx.setStatus(TransactionStatus.PENDING);
        existingTx.setAmount(new BigDecimal("20000.00"));

        // Mock del comportamiento de Jackson
        JsonNode mockNode = mock(JsonNode.class);
        when(objectMapper.readTree(jsonMessage)).thenReturn(mockNode);
        when(mockNode.get("id")).thenReturn(mock(JsonNode.class)); // boilerplate de Jackson mockeado
        when(mockNode.get("id").asText()).thenReturn(txId.toString());
        when(mockNode.get("amount")).thenReturn(mock(JsonNode.class));
        when(mockNode.get("amount").asText()).thenReturn("20000.00");

        // Mock del Repo: Cuando busquen ID, devuelve nuestra tx
        when(transactionRepository.findById(txId)).thenReturn(Optional.of(existingTx));

        // WHEN (Ejecutar la acción)
        transactionConsumer.consumeTransaction(jsonMessage);

        // THEN (Verificar resultados)
        // Usamos Captor para ver qué objeto intentó guardar el código
        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(txCaptor.capture());

        Transaction savedTx = txCaptor.getValue();
        assertEquals(TransactionStatus.FRAUD_DETECTED, savedTx.getStatus());
        System.out.println("✅ Test Pasado: Fraude detectado correctamente.");
    }
}