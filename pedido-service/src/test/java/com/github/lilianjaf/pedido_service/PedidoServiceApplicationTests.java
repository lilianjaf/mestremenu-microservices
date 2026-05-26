package com.github.lilianjaf.pedido_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import test.TestPedidoServiceApplication;

@SpringBootTest(classes = TestPedidoServiceApplication.class)
@ActiveProfiles("test")
class PedidoServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
