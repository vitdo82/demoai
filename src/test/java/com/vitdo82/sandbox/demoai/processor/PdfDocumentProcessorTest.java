package com.vitdo82.sandbox.demoai.processor;

import com.vitdo82.sandbox.demoai.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;

import static org.mockito.Mockito.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PdfDocumentProcessorTest {

    @MockBean
    private VectorStore vectorStore;

    @Autowired
    private PdfDocumentProcessor documentProcessor;

    @Value("classpath:Apress.Master.React.in.5.Days.pdf")
    private Resource resource;

    @Test
    void shouldProcessDocumentWhenGivenValidResource() {
        documentProcessor.processDocument(resource);

        verify(vectorStore).accept(anyList());
    }
}