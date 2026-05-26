package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoItemPedido;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoPedido;
import com.github.lilianjaf.pedido_service.core.exception.DomainException;
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
class CriarPedidoUseCaseIT {

    @Autowired
    private CriarPedidoUseCase criarPedidoUseCase;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private UUID autenticarComo(UUID userId) {
        var auth = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return userId;
    }

    @Test
    @DisplayName("Deve criar e persistir pedido com sucesso")
    void deveCriarPedidoComSucesso() {
        UUID clienteId = autenticarComo(UUID.randomUUID());
        UUID restauranteId = UUID.randomUUID();
        DadosCriacaoPedido dados = new DadosCriacaoPedido(restauranteId,
                List.of(new DadosCriacaoItemPedido("Pizza", 2, BigDecimal.valueOf(25))));

        Pedido pedido = criarPedidoUseCase.executar(dados);

        assertNotNull(pedido.getId());
        assertEquals(clienteId, pedido.getClienteId());
        assertEquals(restauranteId, pedido.getRestauranteId());
        assertEquals(StatusPedido.RASCUNHO, pedido.getStatus());
        assertEquals(BigDecimal.valueOf(50), pedido.getValorTotal());
        assertEquals(1, pedido.getItens().size());
    }

    @Test
    @DisplayName("Deve lançar DomainException quando itens estiverem vazios")
    void deveLancarExcecaoComItensVazios() {
        autenticarComo(UUID.randomUUID());
        DadosCriacaoPedido dados = new DadosCriacaoPedido(UUID.randomUUID(), List.of());

        assertThrows(DomainException.class, () -> criarPedidoUseCase.executar(dados));
    }
}
