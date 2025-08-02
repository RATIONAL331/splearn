package com.example.splearn;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {SplearnTestConfiguration.class})
class SplearnApplicationTests {

	@Test
	void contextLoads() {
		try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
			SplearnApplication.main(new String[0]);

			mocked.verify(() -> SpringApplication.run(SplearnApplication.class, new String[0]));
		}

	}

}
