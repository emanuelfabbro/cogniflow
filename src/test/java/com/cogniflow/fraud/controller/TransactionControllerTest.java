// package com.cogniflow.fraud.controller;

// import com.cogniflow.fraud.dto.TransactionRequest;
// import com.cogniflow.fraud.kafka.producer.TransactionProducer;
// import com.cogniflow.fraud.model.Transaction;
// import com.cogniflow.fraud.model.TransactionStatus;
// import com.cogniflow.fraud.repository.TransactionRepository;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.Test;
// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;

// import java.math.BigDecimal;
// import java.util.UUID;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(TransactionController.class) // Solo carga el Controller, no toda
// la App
// class TransactionControllerTest {

// @Autowired
// private MockMvc mockMvc; // Simula el cliente HTTP (Postman)

// @MockitoBean // Spring crea un Mock y lo inyecta en el Controller
// private TransactionProducer transactionProducer;

// @MockitoBean
// private TransactionRepository transactionRepository;

// @MockitoBean
// private ModelMapper modelMapper;

// @Autowired
// private ObjectMapper objectMapper; // Para convertir objetos a JSON string

// @Test
// void createTransaction_WhenValidRequest_ShouldReturn200() throws Exception {
// // GIVEN
// TransactionRequest request = new TransactionRequest();
// request.setAccountId(UUID.randomUUID());
// request.setAmount(new BigDecimal("100.00"));
// request.setCurrency("USD");

// Transaction transactionEntity = new Transaction();
// transactionEntity.setId(UUID.randomUUID());
// transactionEntity.setStatus(TransactionStatus.PENDING);
// transactionEntity.setAmount(new BigDecimal("100.00"));

// // Mockeamos el Mapper y el Repo
// when(modelMapper.map(any(), any())).thenReturn(transactionEntity);
// when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionEntity);

// // WHEN & THEN
// mockMvc.perform(post("/api/v1/transactions")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(request)))
// .andExpect(status().isOk()) // Esperamos HTTP 200
// .andExpect(jsonPath("$.status").value("PENDING")) // Verificamos el JSON de
// respuesta
// .andExpect(jsonPath("$.amount").value(100.00));
// }

// @Test
// void createTransaction_WhenInvalidAmount_ShouldReturn400() throws Exception {
// // GIVEN (Monto negativo para disparar @Positive)
// TransactionRequest request = new TransactionRequest();
// request.setAccountId(UUID.randomUUID());
// request.setAmount(new BigDecimal("-50.00"));
// request.setCurrency("USD");

// // WHEN & THEN
// mockMvc.perform(post("/api/v1/transactions")
// .contentType(MediaType.APPLICATION_JSON)
// .content(objectMapper.writeValueAsString(request)))
// .andExpect(status().isBadRequest()); // Esperamos HTTP 400
// }
// }