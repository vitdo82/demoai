package com.vitdo82.sandbox.demoai.processor;

import com.vitdo82.sandbox.demoai.service.EmbeddingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class PdfDocumentProcessor implements DocumentProcessor {

    private final EmbeddingService embeddingService;

    @Override
    public void processDocument(Resource resource) {
        log.info("Start document processing, name: {}", resource.getFilename());
        DocumentReader reader;
        try {
            reader = new ParagraphPdfDocumentReader(resource);
        } catch (IllegalArgumentException e) {
            log.warn("Error parse file '{}' with 'ParagraphPdfDocumentReader' switch to 'PagePdfDocumentReader'", resource.getFilename());
            reader = new PagePdfDocumentReader(resource);
        }

        TextSplitter splitter = new TokenTextSplitter();
        List<Document> docs = splitter.apply(reader.get());

        log.info("Upload {} documents in vector store", docs.size());
        embeddingService.generateAndStoreEmbedding(docs.stream().map(Document::getContent).collect(Collectors.joining()), resource.getFilename());

        log.info("Finish document processing, name: {}", resource.getFilename());
    }
}
