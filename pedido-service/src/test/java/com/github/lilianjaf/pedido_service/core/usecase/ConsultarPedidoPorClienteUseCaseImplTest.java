package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import com.github.lilianjaf.pedido_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.rules.ConsultarPedidoPorClienteContext;
import com.github.lilianjaf.pedido_service.core.rules.ConsultarPedidoPorClienteRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarPedidoPorClienteUseCaseImplTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    @Mock private ConsultarPedidoPorClienteRule permissaoRule;
    @Mock private ConsultarPedidoPorClienteRule businessRule;

    private ConsultarPedidoPorClienteUseCaseImpl useCase;

    private final UUID clienteId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        useCase = new ConsultarPedidoPorClienteUseCaseImpl(
                pedidoRepository, obterUsuarioLogadoGateway,
                List.of(permissaoRule), List.of(businessRule));
    }

    @Test
    @DisplayName("Deve retornar lista de pedidos do cliente autenticado")
    void deveRetornarPedidosDoCliente() {
        Pedido pedido = new Pedido(UUID.randomUUID(), clienteId, UUID.randomUUID(),
                List.of(new ItemPedido("Lanche", 1, BigDecimal.TEN)),
                BigDecimal.TEN, StatusPedido.CRIADO, LocalDateTime.now());

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(new Usuario(clienteId)));
        when(pedidoRepository.buscarPorClienteId(clienteId)).thenReturn(List.of(pedido));

        List<Pedido> resultado = useCase.executar();

        assertEquals(1, resultado.size());
        verify(permissaoRule).validar(any(ConsultarPedidoPorClienteContext.class));
        verify(businessRule).validar(any(ConsultarPedidoPorClienteContext.class));
        verify(pedidoRepository).buscarPorClienteId(clienteId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando cliente não tem pedidos")
    void deveRetornarListaVaziaQuandoSemPedidos() {
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(new Usuario(clienteId)));
        when(pedidoRepository.buscarPorClienteId(clienteId)).thenReturn(List.of());

        List<Pedido> resultado = useCase.executar();

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não está autenticado")
    void deveLancarExcecaoQuandoUsuarioNaoAutenticado() {
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoAutenticadoException.class, () -> useCase.executar());
        verify(pedidoRepository, never()).buscarPorClienteId(any());
    }
}
