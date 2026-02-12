package com.cogniflow.fraud.kafka.consumer;

import com.cogniflow.fraud.config.KafkaConfig;
import com.cogniflow.fraud.model.Transaction;
import com.cogniflow.fraud.model.TransactionStatus;
import com.cogniflow.fraud.repository.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionConsumer {

    private final TransactionRepository transactionRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaConfig.FRAUD_TOPIC, groupId = "fraud-group")
    @Transactional // Asegura que las escrituras en DB sean atómicas
    public void consumeTransaction(String message) {
        log.info("🔍 Analizando transacción: {}", message);

        try {
            // 1. Deserializar el mensaje JSON
            JsonNode jsonNode = objectMapper.readTree(message);
            String transactionIdStr = jsonNode.get("id").asText();
            BigDecimal amount = new BigDecimal(jsonNode.get("amount").asText());
            UUID transactionId = UUID.fromString(transactionIdStr);

            // 2. Buscar la transacción en BD (Para asegurar que existe y actualizarla)
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new RuntimeException("Transacción no encontrada en BD: " + transactionId));

            // 3. 🛡️ MOTOR DE REGLAS DE FRAUDE (Simulado)
            // Regla 1: Si el monto es mayor a 10,000, es sospechoso.
            // Regla 2: (Podrías agregar más aquí, ej: IPs de riesgo)
            boolean isFraud = amount.compareTo(new BigDecimal("10000")) > 0;

            if (isFraud) {
                log.warn("🚨 FRAUDE DETECTADO: La transacción {} excede el límite de seguridad.", transactionId);
                transaction.setStatus(TransactionStatus.FRAUD_DETECTED);

                // Aquí podrías disparar otra alerta a un tópico 'fraud-alerts'
                // o guardar en la tabla 'fraud_alerts'
            } else {
                log.info("✅ Transacción aprobada: {}", transactionId);
                transaction.setStatus(TransactionStatus.APPROVED);
            }

            // 4. Guardar el nuevo estado
            transactionRepository.save(transaction);

        } catch (JsonProcessingException e) {
            log.error("❌ Error al leer JSON de Kafka: {}", message, e);
            // En un sistema real, enviarías esto a un "Dead Letter Queue" (DLQ)
        } catch (Exception e) {
            log.error("❌ Error procesando transacción", e);
        }
    }
}