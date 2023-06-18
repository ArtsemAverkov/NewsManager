package ru.clevertec.NewsManager;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.NewsManager.common.WireMockInitializer;

@SpringBootTest
class NewsManagerApplicationTests {

	@BeforeAll
	static void setup() {
		WireMockInitializer.setup();
	}

	@AfterAll
	static void teardown() {
		WireMockInitializer.teardown();
	}

	@Test
	void contextLoads() {
	}

}
