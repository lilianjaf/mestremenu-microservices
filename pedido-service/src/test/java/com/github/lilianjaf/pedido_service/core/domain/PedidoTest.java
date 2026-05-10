package com.github.lilianjaf.pedido_service.core.domain;

import com.github.lilianjaf.pedido_service.core.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private Pedido pedidoEmRascunho() {
        return new Pedido(UUID.randomUUID(), UUID.randomUUID(),
                List.of(new ItemPedido("Lanche", 1, BigDecimal.TEN)));
    }

    private Pedido pedidoComStatus(StatusPedido status) {
        return new Pedido(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
                List.of(new ItemPedido("Lanche", 1, BigDecimal.TEN)),
                BigDecimal.TEN, status, LocalDateTime.now());
    }

    @Test
    @DisplayName("Novo pedido deve iniciar em RASCUNHO com valor total calculado")
    void novoPedidoDeveIniciarEmRascunho() {
        ItemPedido item = new ItemPedido("Pizza", 2, new BigDecimal("25.00"));
        Pedido pedido = new Pedido(UUID.randomUUID(), UUID.randomUUID(), List.of(item));

        assertEquals(StatusPedido.RASCUNHO, pedido.getStatus());
        assertEquals(new BigDecimal("50.00"), pedido.getValorTotal());
        assertNotNull(pedido.getId());
    }

    @Test
    @DisplayName("confirmar() deve mudar status para CRIADO quando em RASCUNHO")
    void confirmarDeveMudarStatusParaCriado() {
        Pedido pedido = pedidoEmRascunho();
        pedido.confirmar();
        assertEquals(StatusPedido.CRIADO, pedido.getStatus());
    }

    @Test
    @DisplayName("confirmar() deve lançar exceção quando pedido não está em RASCUNHO")
    void confirmarDeveLancarExcecaoQuandoNaoEstaEmRascunho() {
        Pedido pedido = pedidoComStatus(StatusPedido.CRIADO);
        assertThrows(DomainException.class, pedido::confirmar);
    }

    @Test
    @DisplayName("atualizarStatus() deve aceitar PAGO a partir de CRIADO")
    void atualizarStatusDeveAceitarPagoDeCreado() {
        Pedido pedido = pedidoComStatus(StatusPedido.CRIADO);
        pedido.atualizarStatus(StatusPedido.PAGO);
        assertEquals(StatusPedido.PAGO, pedido.getStatus());
    }

    @Test
    @DisplayName("atualizarStatus() deve aceitar PAGO a partir de PENDENTE_PAGAMENTO")
    void atualizarStatusDeveAceitarPagoDePendente() {
        Pedido pedido = pedidoComStatus(StatusPedido.PENDENTE_PAGAMENTO);
        pedido.atualizarStatus(StatusPedido.PAGO);
        assertEquals(StatusPedido.PAGO, pedido.getStatus());
    }

    @Test
    @DisplayName("atualizarStatus() deve aceitar PENDENTE_PAGAMENTO a partir de CRIADO")
    void atualizarStatusDeveAceitarPendenteDeCriado() {
        Pedido pedido = pedidoComStatus(StatusPedido.CRIADO);
        pedido.atualizarStatus(StatusPedido.PENDENTE_PAGAMENTO);
        assertEquals(StatusPedido.PENDENTE_PAGAMENTO, pedido.getStatus());
    }

    @Test
    @DisplayName("atualizarStatus() deve aceitar CANCELADO a partir de CRIADO")
    void atualizarStatusDeveAceitarCanceladoDeCriado() {
        Pedido pedido = pedidoComStatus(StatusPedido.CRIADO);
        pedido.atualizarStatus(StatusPedido.CANCELADO);
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
    }

    @Test
    @DisplayName("atualizarStatus() deve aceitar CANCELADO a partir de PENDENTE_PAGAMENTO")
    void atualizarStatusDeveAceitarCanceladoDePendente() {
        Pedido pedido = pedidoComStatus(StatusPedido.PENDENTE_PAGAMENTO);
        pedido.atualizarStatus(StatusPedido.CANCELADO);
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
    }

    @Test
    @DisplayName("atualizarStatus() deve lançar exceção para transição inválida")
    void atualizarStatusDeveLancarExcecaoParaTransicaoInvalida() {
        Pedido pedido = pedidoComStatus(StatusPedido.PAGO);
        assertThrows(DomainException.class, () -> pedido.atualizarStatus(StatusPedido.CANCELADO));
    }
}
