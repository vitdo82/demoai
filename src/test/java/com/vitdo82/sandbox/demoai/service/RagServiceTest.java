package com.vitdo82.sandbox.demoai.service;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RagServiceTest {

    @Autowired
    private RagService ragService;

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
}