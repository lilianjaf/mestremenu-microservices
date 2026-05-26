package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoItemPedido;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoPedido;
import com.github.lilianjaf.pedido_service.core.exception.AcessoNegadoAoPedidoException;
import com.github.lilianjaf.pedido_service.core.exception.PedidoNaoEncontradoException;
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
class ConsultarPedidoUseCaseIT {

    @Autowired
    private CriarPedidoUseCase criarPedidoUseCase;

    @Autowired
    private ConsultarPedidoUseCase consultarPedidoUseCase;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private UUID autenticarComo(UUID userId) {
        var auth = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return userId;
    }

    private Pedido criarPedido(UUID clienteId) {
        autenticarComo(clienteId);
        return criarPedidoUseCase.executar(new DadosCriacaoPedido(
                UUID.randomUUID(),
                List.of(new DadosCriacaoItemPedido("Burger", 1, BigDecimal.valueOf(20)))));
    }

    @Test
    @DisplayName("Deve retornar pedido existente com itens")
    void deveRetornarPedidoExistente() {
        UUID clienteId = UUID.randomUUID();
        Pedido criado = criarPedido(clienteId);
        autenticarComo(clienteId);

        Pedido consultado = consultarPedidoUseCase.executar(criado.getId());

        assertEquals(criado.getId(), consultado.getId());
        assertEquals(clienteId, consultado.getClienteId());
        assertFalse(consultado.getItens().isEmpty());
    }

    @Test
    @DisplayName("Deve lançar PedidoNaoEncontradoException para ID inexistente")
    void deveLancarExcecaoParaPedidoInexistente() {
        autenticarComo(UUID.randomUUID());

        assertThrows(PedidoNaoEncontradoException.class,
                () -> consultarPedidoUseCase.executar(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Deve lançar AcessoNegadoAoPedidoException quando pedido não pertence ao cliente")
    void deveLancarExcecaoQuandoPedidoNaoPertenceAoCliente() {
        UUID clienteA = UUID.randomUUID();
        Pedido pedido = criarPedido(clienteA);
        autenticarComo(UUID.randomUUID());

        assertThrows(AcessoNegadoAoPedidoException.class,
                () -> consultarPedidoUseCase.executar(pedido.getId()));
    }
}
