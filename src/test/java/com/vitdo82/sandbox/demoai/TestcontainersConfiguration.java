package com.vitdo82.sandbox.demoai;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

//    @Bean
//    @ServiceConnection
//    OllamaContainer ollamaContainer() {
//        return new OllamaContainer(DockerImageName.parse("ollama/ollama:latest"));
//    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> pgvectorContainer() {
//        return new PostgreSQLContainer<>(DockerImageName.parse("pgvector/pgvector:pg16"));
        return new PostgreSQLContainer<>(DockerImageName.parse("ankane/pgvector:latest")
                .asCompatibleSubstituteFor("postgres"));
    }

//    @Bean
//    @ServiceConnection
//    PostgreSQLContainer<?> postgresContainer() {
//        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
//    }
}
