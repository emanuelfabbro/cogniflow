package com.cogniflow.fraud.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 💡 TIP SENIOR:
        // Registramos este módulo para que Jackson sepa convertir
        // LocalDateTime (Java 8) a formato JSON sin romper todo.
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}