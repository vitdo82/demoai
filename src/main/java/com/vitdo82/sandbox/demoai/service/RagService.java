package com.vitdo82.sandbox.demoai.service;

import com.vitdo82.sandbox.demoai.model.VectorStoreEntity;
import com.vitdo82.sandbox.demoai.model.VectorStoreRepository;
import com.vitdo82.sandbox.demoai.processor.DocumentProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@ConditionalOnProperty("spring.datasource.url")
public class RagService {

    private ChatModel chatModel;
    private VectorStore vectorStore;

    private final Map<String, DocumentProcessor> documentProcessors;

    @Getter
    private final Set<String> uploadedFileName = new HashSet<>();
    private final VectorStoreRepository vectorStoreRepository;

    public List<VectorStoreEntity> findAll(String name) {
        return vectorStoreRepository.findVectorStoreEntitiesByDocument_DocumentName(name);
    }

    public void processFiles(List<MultipartFile> multipartFiles) {
        for (MultipartFile file : multipartFiles) {
            String fileType = determineFileType(file);
            DocumentProcessor documentProcessor = documentProcessors.get(fileType);
            if (documentProcessor == null) {
                throw new IllegalArgumentException("No processor found for file type: " + fileType);
            }

            documentProcessor.processDocument(file.getResource());
            uploadedFileName.add(file.getOriginalFilename());
        }
    }

    public Flux<ChatResponse> query(String question) {
        List<Document> similarDocuments = vectorStore.similaritySearch(
            SearchRequest
                .query(question)
                .withTopK(5)
                .withFilterExpression(new Filter.Expression(
                    Filter.ExpressionType.EQ, new Filter.Key("category"), new Filter.Value("PUBLIC")
                ))
        );

        log.debug("Retrieved {}  similar docs.", similarDocuments.size());
        String context = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));

        String prompt = "Context: " + context + "\n\nQuestion: " + question + "\n\nAnswer:";
        log.debug("The prompt is {}", prompt);
        return chatModel.stream(new Prompt(prompt));
    }

    private String determineFileType(MultipartFile multipartFile) {
        if ("application/pdf".equals(multipartFile.getContentType())) {
            return "pdfDocumentProcessor";
        }
        return "";
    }
}
