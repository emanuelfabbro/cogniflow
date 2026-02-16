package com.cogniflow.fraud.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Data
@Builder
@Document(indexName = "transactions_idx") // El nombre del índice en Elastic
public class TransactionDocument {

    @Id
    private String id;

    @Field(type = FieldType.Keyword) // Keyword = Búsqueda exacta
    private String accountId;

    @Field(type = FieldType.Double)
    private BigDecimal amount;

    @Field(type = FieldType.Text) // Text = Búsqueda full-text
    private String status;
}