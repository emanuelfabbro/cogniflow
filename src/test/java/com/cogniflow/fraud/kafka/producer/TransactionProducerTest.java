package com.cogniflow.fraud.kafka.producer;

import com.cogniflow.fraud.config.KafkaConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private TransactionProducer transactionProducer;

    @Test
    void sendTransaction_ShouldCallKafkaTemplate() {
        // GIVEN
        String message = "{\"id\":\"123\", \"amount\":500}";

        // WHEN
        transactionProducer.sendTransaction(message);

        // THEN
        // Verificamos que el método send() del template fue llamado
        // con el tópico correcto y el mensaje correcto.
        verify(kafkaTemplate).send(eq(KafkaConfig.FRAUD_TOPIC), eq(message));
    }
}