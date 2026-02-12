package com.cogniflow.fraud.kafka.producer;

import com.cogniflow.fraud.config.KafkaConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j // Lombok para logging automático
@RequiredArgsConstructor // Inyección de dependencias por constructor
public class TransactionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendTransaction(String transactionJson) {
        log.info("Enviando transacción a Kafka: {}", transactionJson);
        // Enviamos al tópico definido en la config
        kafkaTemplate.send(KafkaConfig.FRAUD_TOPIC, transactionJson);
    }
}