package com.github.lilianjaf.restaurante_service;

import com.github.lilianjaf.usuario_service.core.api.UsuarioModuleFacade;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class RestauranteServiceApplicationTests {

	@MockBean
	UsuarioModuleFacade usuarioModuleFacade;

	@Test
	void contextLoads() {
	}

}
