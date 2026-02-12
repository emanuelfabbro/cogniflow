package com.cogniflow.fraud.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cogniflow.fraud.dto.TransactionRequest;
import com.cogniflow.fraud.model.Transaction;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Configuración para que ignore ambigüedades y sea estricto
        modelMapper.getConfiguration().setAmbiguityIgnored(true);

        // Opcional: Ignorar explícitamente el seteo del ID durante el mapeo
        modelMapper.typeMap(TransactionRequest.class, Transaction.class)
                .addMappings(mapper -> mapper.skip(Transaction::setId));

        return modelMapper;
    }
}