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

class ConfirmarPedidoContextTest {

    private final UUID clienteId = UUID.randomUUID();
    private final Usuario usuario = new Usuario(clienteId);

    private Pedido pedido(UUID cliente, StatusPedido status) {
        return new Pedido(UUID.randomUUID(), cliente, UUID.randomUUID(),
                List.of(), BigDecimal.ZERO, status, LocalDateTime.now());
    }

    @Test
    @DisplayName("isUsuarioAutenticado retorna true quando usuário tem ID")
    void isUsuarioAutenticado_trueQuandoTemId() {
        assertTrue(new ConfirmarPedidoContext(usuario, pedido(clienteId, StatusPedido.RASCUNHO)).isUsuarioAutenticado());
    }

    @Test
    @DisplayName("isUsuarioAutenticado retorna false quando usuário é null")
    void isUsuarioAutenticado_falseQuandoNull() {
        assertFalse(new ConfirmarPedidoContext(null, pedido(clienteId, StatusPedido.RASCUNHO)).isUsuarioAutenticado());
    }

    @Test
    @DisplayName("isPedidoDoCliente retorna true quando clienteId bate")
    void isPedidoDoCliente_trueQuandoClienteId() {
        assertTrue(new ConfirmarPedidoContext(usuario, pedido(clienteId, StatusPedido.RASCUNHO)).isPedidoDoCliente());
    }

    @Test
    @DisplayName("isPedidoDoCliente retorna false quando clienteId diferente")
    void isPedidoDoCliente_falseQuandoOutroCliente() {
        assertFalse(new ConfirmarPedidoContext(usuario, pedido(UUID.randomUUID(), StatusPedido.RASCUNHO)).isPedidoDoCliente());
    }

    @Test
    @DisplayName("isPedidoEmRascunho retorna true quando status é RASCUNHO")
    void isPedidoEmRascunho_trueParaRascunho() {
        assertTrue(new ConfirmarPedidoContext(usuario, pedido(clienteId, StatusPedido.RASCUNHO)).isPedidoEmRascunho());
    }

    @Test
    @DisplayName("isPedidoEmRascunho retorna false quando status é CRIADO")
    void isPedidoEmRascunho_falseParaCriado() {
        assertFalse(new ConfirmarPedidoContext(usuario, pedido(clienteId, StatusPedido.CRIADO)).isPedidoEmRascunho());
    }
}
