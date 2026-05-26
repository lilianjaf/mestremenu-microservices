package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ConsultarPedidoPorClienteContextTest {

    @Test
    @DisplayName("isUsuarioAutenticado retorna true quando usuário tem ID")
    void isUsuarioAutenticado_trueQuandoTemId() {
        assertTrue(new ConsultarPedidoPorClienteContext(new Usuario(UUID.randomUUID())).isUsuarioAutenticado());
    }

    @Test
    @DisplayName("isUsuarioAutenticado retorna false quando usuário é null")
    void isUsuarioAutenticado_falseQuandoNull() {
        assertFalse(new ConsultarPedidoPorClienteContext(null).isUsuarioAutenticado());
    }
}
