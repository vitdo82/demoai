package com.vitdo82.sandbox.demoai.processor;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class PdfDocumentProcessor implements DocumentProcessor {

    private final VectorStore vectorStore;

    @Override
    public void processDocument(Resource resource) {
        ParagraphPdfDocumentReader reader = new ParagraphPdfDocumentReader(resource);
        TextSplitter splitter = new TokenTextSplitter();
        List<Document> docs = splitter.apply(reader.get());

        docs.stream().map(Document::getContent).forEach(log::debug);

        vectorStore.accept(docs);
    }
}
