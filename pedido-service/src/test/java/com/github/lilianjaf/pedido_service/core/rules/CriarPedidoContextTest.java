package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CriarPedidoContextTest {

    private final UUID restauranteId = UUID.randomUUID();
    private final Usuario usuario = new Usuario(UUID.randomUUID());
    private final ItemPedido item = new ItemPedido("Lanche", 1, BigDecimal.TEN);

    @Test
    @DisplayName("isUsuarioAutenticado retorna true quando usuário tem ID")
    void isUsuarioAutenticado_trueQuandoTemId() {
        assertTrue(new CriarPedidoContext(usuario, restauranteId, List.of(item)).isUsuarioAutenticado());
    }

    @Test
    @DisplayName("isUsuarioAutenticado retorna false quando usuário é null")
    void isUsuarioAutenticado_falseQuandoNull() {
        assertFalse(new CriarPedidoContext(null, restauranteId, List.of(item)).isUsuarioAutenticado());
    }

    @Test
    @DisplayName("isItensPedidoVazio retorna true quando lista é vazia")
    void isItensPedidoVazio_trueQuandoVazio() {
        assertTrue(new CriarPedidoContext(usuario, restauranteId, Collections.emptyList()).isItensPedidoVazio());
    }

    @Test
    @DisplayName("isItensPedidoVazio retorna false quando lista tem itens")
    void isItensPedidoVazio_falseQuandoTemItens() {
        assertFalse(new CriarPedidoContext(usuario, restauranteId, List.of(item)).isItensPedidoVazio());
    }

    @Test
    @DisplayName("isItensPedidoVazio retorna true quando lista é null")
    void isItensPedidoVazio_trueQuandoNull() {
        assertTrue(new CriarPedidoContext(usuario, restauranteId, null).isItensPedidoVazio());
    }
}
