package org.taskservice;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

@DirtiesContext
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskServiceApplicationTests {
    private static ConfluentKafkaContainer KAFKA_CONTAINER;

    @BeforeAll
    static void setUpBeforeAll() {
        KAFKA_CONTAINER = new ConfluentKafkaContainer(
                DockerImageName.parse("confluentinc/cp-kafka:7.5.1")
                        .asCompatibleSubstituteFor("apache/kafka")
        );
        KAFKA_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("kafka.task.bootstrap-server", KAFKA_CONTAINER::getBootstrapServers);
    }
}
