package com.vitdo82.sandbox.demoai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest(
    properties = {
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.flyway.url=jdbc:postgresql://localhost:5432/test"
    }
)
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SchemaValidationTest {

    @Test
    void testSchemaValidity() {
    }
}
