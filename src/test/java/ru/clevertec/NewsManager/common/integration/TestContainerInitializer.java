package ru.clevertec.NewsManager.common.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainerInitializer {
    private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:14.3");


    private static PostgreSQLContainer<?> postgresContainer;

    @BeforeAll
    public static void start() {
        postgresContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
                .withDatabaseName("test")
                .withUsername("root")
                .withPassword("root")
                .withExposedPorts(5432);
        postgresContainer.start();
    }


    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
    }

}

