package com.vitdo82.sandbox.demoai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DemoAiApplicationTests {

    @Test
    void contextLoads() {
    }

}
