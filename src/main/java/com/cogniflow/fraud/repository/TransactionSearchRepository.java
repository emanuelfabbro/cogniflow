package com.cogniflow.fraud.repository;

import com.cogniflow.fraud.document.TransactionDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionSearchRepository extends ElasticsearchRepository<TransactionDocument, String> {
    // Spring Data genera la query automáticamente por el nombre del método
    List<TransactionDocument> findByStatus(String status);
}