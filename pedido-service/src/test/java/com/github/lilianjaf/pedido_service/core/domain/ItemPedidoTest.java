package com.github.lilianjaf.pedido_service.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    @DisplayName("Deve criar item com ID gerado automaticamente")
    void deveCriarComIdGerado() {
        ItemPedido item = new ItemPedido("Pizza", 2, BigDecimal.valueOf(25));
        assertNotNull(item.getId());
        assertEquals("Pizza", item.getDescricao());
        assertEquals(2, item.getQuantidade());
        assertEquals(BigDecimal.valueOf(25), item.getPreco());
    }

    @Test
    @DisplayName("Deve criar item com ID fornecido")
    void deveCriarComIdFornecido() {
        UUID id = UUID.randomUUID();
        ItemPedido item = new ItemPedido(id, "Burger", 1, BigDecimal.TEN);
        assertEquals(id, item.getId());
        assertEquals("Burger", item.getDescricao());
        assertEquals(1, item.getQuantidade());
    }

    @Test
    @DisplayName("subtotal deve retornar preco vezes quantidade")
    void subtotalDeveRetornarPrecoVezesQuantidade() {
        ItemPedido item = new ItemPedido("Pizza", 3, BigDecimal.valueOf(20));
        assertEquals(BigDecimal.valueOf(60), item.subtotal());
    }

    @Test
    @DisplayName("subtotal com quantidade 1 deve retornar preco")
    void subtotalComQuantidade1DeveRetornarPreco() {
        ItemPedido item = new ItemPedido("Pizza", 1, BigDecimal.valueOf(15.50));
        assertEquals(BigDecimal.valueOf(15.50), item.subtotal());
    }
}
