package com.vitdo82.sandbox.demoai.service;

import com.vitdo82.sandbox.demoai.model.DocumentEntity;
import com.vitdo82.sandbox.demoai.model.DocumentRepository;
import com.vitdo82.sandbox.demoai.model.VectorStoreEntity;
import com.vitdo82.sandbox.demoai.model.VectorStoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty("spring.datasource.url")
public class EmbeddingService {

    private VectorStoreRepository vectorStoreRepository;
    private DocumentRepository documentRepository;

    private EmbeddingModel embeddingModel;

    public void generateAndStoreEmbedding(String input, String originalFileName) {
        DocumentEntity document = documentRepository.findByDocumentName(originalFileName)
            .orElseGet(() -> documentRepository.save(DocumentEntity.builder()
                .documentName(originalFileName)
                .metadata(Map.of("category", "PUBLIC"))
                .build()));

        vectorStoreRepository.saveAll(List.of(
            VectorStoreEntity.builder()
                .document(document)
                .content(input)
                .embedding(embeddingModel.embed(input))
                .metadata(document.getMetadata())
                .build()
        ));
        log.info("Stored embedding for input: {}", input);
    }
}
