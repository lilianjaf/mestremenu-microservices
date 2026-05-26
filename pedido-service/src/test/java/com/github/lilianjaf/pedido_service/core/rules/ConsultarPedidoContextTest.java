package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ConsultarPedidoContextTest {

    private final UUID clienteId = UUID.randomUUID();
    private final Usuario usuario = new Usuario(clienteId);

    private Pedido pedido(UUID cliente) {
        return new Pedido(UUID.randomUUID(), cliente, UUID.randomUUID(),
                List.of(), BigDecimal.ZERO, StatusPedido.CRIADO, LocalDateTime.now());
    }

    @Test
    @DisplayName("isUsuarioAutenticado retorna true quando usuário tem ID")
    void isUsuarioAutenticado_trueQuandoTemId() {
        assertTrue(new ConsultarPedidoContext(usuario, pedido(clienteId)).isUsuarioAutenticado());
    }

    @Test
    @DisplayName("isUsuarioAutenticado retorna false quando usuário é null")
    void isUsuarioAutenticado_falseQuandoNull() {
        assertFalse(new ConsultarPedidoContext(null, pedido(clienteId)).isUsuarioAutenticado());
    }

    @Test
    @DisplayName("isPedidoDoCliente retorna true quando clienteId bate")
    void isPedidoDoCliente_trueQuandoClienteId() {
        assertTrue(new ConsultarPedidoContext(usuario, pedido(clienteId)).isPedidoDoCliente());
    }

    @Test
    @DisplayName("isPedidoDoCliente retorna false quando clienteId diferente")
    void isPedidoDoCliente_falseQuandoOutroCliente() {
        assertFalse(new ConsultarPedidoContext(usuario, pedido(UUID.randomUUID())).isPedidoDoCliente());
    }
}
