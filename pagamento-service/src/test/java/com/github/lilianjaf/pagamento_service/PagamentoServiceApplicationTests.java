package com.github.lilianjaf.pagamento_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import test.TestPagamentoServiceApplication;

@SpringBootTest(classes = TestPagamentoServiceApplication.class)
@ActiveProfiles("test")
class PagamentoServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
