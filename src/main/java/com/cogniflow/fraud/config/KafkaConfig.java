package com.cogniflow.fraud.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    // Nombre del tópico como constante (Buena práctica)
    public static final String FRAUD_TOPIC = "fraud-transactions";

    @Bean
    public NewTopic fraudTopic() {
        // Tip de Entrevista:
        // "Defino 3 particiones para permitir concurrencia.
        // Si escalamos a 3 instancias del microservicio, cada una leerá de una
        // partición."
        return TopicBuilder.name(FRAUD_TOPIC)
                .partitions(3)
                .replicas(1) // 1 porque estamos en Docker local (single node). En PROD serían 3.
                .build();
    }
}