package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoItemPedido;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoPedido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import test.TestPedidoServiceApplication;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TestPedidoServiceApplication.class)
@ActiveProfiles("test")
@Transactional
class ConsultarPedidoPorClienteUseCaseIT {

    @Autowired
    private CriarPedidoUseCase criarPedidoUseCase;

    @Autowired
    private ConsultarPedidoPorClienteUseCase consultarPedidoPorClienteUseCase;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private UUID autenticarComo(UUID userId) {
        var auth = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return userId;
    }

    private void criarPedido() {
        criarPedidoUseCase.executar(new DadosCriacaoPedido(
                UUID.randomUUID(),
                List.of(new DadosCriacaoItemPedido("Pizza", 1, BigDecimal.valueOf(30)))));
    }

    @Test
    @DisplayName("Deve retornar pedidos do cliente autenticado")
    void deveRetornarPedidosDoCliente() {
        UUID clienteId = autenticarComo(UUID.randomUUID());
        criarPedido();
        criarPedido();

        List<Pedido> pedidos = consultarPedidoPorClienteUseCase.executar();

        assertEquals(2, pedidos.size());
        pedidos.forEach(p -> assertEquals(clienteId, p.getClienteId()));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando cliente não tem pedidos")
    void deveRetornarListaVaziaQuandoSemPedidos() {
        autenticarComo(UUID.randomUUID());

        List<Pedido> pedidos = consultarPedidoPorClienteUseCase.executar();

        assertTrue(pedidos.isEmpty());
    }
}
