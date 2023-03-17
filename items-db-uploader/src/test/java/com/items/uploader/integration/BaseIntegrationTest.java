package com.items.uploader.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

    // MongoDBContainer is not working correctly with init scripts
    // see https://github.com/testcontainers/testcontainers-java/issues/3066#
    @Container
    static GenericContainer  mongoDBContainer = new GenericContainer(DockerImageName.parse("mongo:latest"))
            .withClasspathResourceMapping("/mongo-init.js", "/docker-entrypoint-initdb.d/mongo-init.js", BindMode.READ_ONLY)
            .withExposedPorts(27017);

    @Container
    static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.6.2"))
            .withEnv("xpack.security.enabled", "false");

    @DynamicPropertySource
    static void bootstrapProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.data.mongodb.port", () -> mongoDBContainer.getFirstMappedPort());

        // additional property for tests
        registry.add("spring.elasticsearch.uris", () -> elasticsearchContainer.getHttpHostAddress());
    }
}
