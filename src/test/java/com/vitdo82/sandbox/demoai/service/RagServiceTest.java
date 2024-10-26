package com.vitdo82.sandbox.demoai.service;

import com.vitdo82.sandbox.demoai.TestcontainersConfiguration;
import com.vitdo82.sandbox.demoai.processor.PdfDocumentProcessor;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class RagServiceTest {

    @MockBean
    private PdfDocumentProcessor documentProcessor;

    @Autowired
    private RagService ragService;

    @Value("classpath:Apress.Master.React.in.5.Days.pdf")
    private Resource resource;

    @Ignore
    void testQueryExecution() {
        String query = """
                Spring AI rocks
                """;

        String result = ragService.query(query)
            .timeout(Duration.ofSeconds(10))
            .map(response -> response.getResult().getOutput().getContent())
            .collect(Collectors.joining())
            .block();

        assertThat(result).isNotNull();
    }

    @Test
    void testProcessFiles() throws IOException {
        assertThat(resource).isNotNull();

        String fileName = Objects.requireNonNull(resource.getFilename());
        Path path = Paths.get(resource.getURI());

        MockMultipartFile file = new MockMultipartFile(fileName, fileName, Files.probeContentType(path), resource.getInputStream());
        ragService.processFiles(List.of(file));

        assertThat(ragService.getUploadedFileName()).hasSize(1).containsExactly("Apress.Master.React.in.5.Days.pdf");
    }
}