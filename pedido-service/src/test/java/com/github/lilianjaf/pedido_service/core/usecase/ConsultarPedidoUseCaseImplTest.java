package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import com.github.lilianjaf.pedido_service.core.exception.PedidoNaoEncontradoException;
import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import com.github.lilianjaf.pedido_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.pedido_service.core.rules.ConsultarPedidoContext;
import com.github.lilianjaf.pedido_service.core.rules.ConsultarPedidoRule;
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
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarPedidoUseCaseImplTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    @Mock private TransactionGateway transactionGateway;
    @Mock private ConsultarPedidoRule permissaoRule;
    @Mock private ConsultarPedidoRule businessRule;

    private ConsultarPedidoUseCaseImpl useCase;

    private final UUID clienteId = UUID.randomUUID();
    private final UUID pedidoId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        lenient().doAnswer(inv -> ((Supplier<?>) inv.getArgument(0)).get())
                .when(transactionGateway).execute(any(Supplier.class));
        useCase = new ConsultarPedidoUseCaseImpl(
                pedidoRepository, obterUsuarioLogadoGateway, transactionGateway,
                List.of(permissaoRule), List.of(businessRule));
    }

    private Pedido pedidoCriado() {
        return new Pedido(pedidoId, clienteId, UUID.randomUUID(),
                List.of(new ItemPedido("Lanche", 1, BigDecimal.TEN)),
                BigDecimal.TEN, StatusPedido.CRIADO, LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve retornar o pedido quando usuário autenticado e pedido existe")
    void deveRetornarPedidoComSucesso() {
        Pedido pedido = pedidoCriado();
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(new Usuario(clienteId)));
        when(pedidoRepository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedido));

        Pedido resultado = useCase.executar(pedidoId);

        assertEquals(pedidoId, resultado.getId());
        verify(permissaoRule).validar(any(ConsultarPedidoContext.class));
        verify(businessRule).validar(any(ConsultarPedidoContext.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não está autenticado")
    void deveLancarExcecaoQuandoUsuarioNaoAutenticado() {
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoAutenticadoException.class, () -> useCase.executar(pedidoId));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não existe")
    void deveLancarExcecaoQuandoPedidoNaoExiste() {
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(new Usuario(clienteId)));
        when(pedidoRepository.buscarPorId(pedidoId)).thenReturn(Optional.empty());

        assertThrows(PedidoNaoEncontradoException.class, () -> useCase.executar(pedidoId));
    }

    @Test
    @DisplayName("Deve lançar exceção quando regra de permissão é violada")
    void deveLancarExcecaoQuandoRegraDePermissaoViolada() {
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(new Usuario(clienteId)));
        when(pedidoRepository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedidoCriado()));
        doThrow(new RuntimeException("Acesso negado")).when(permissaoRule).validar(any());

        assertThrows(RuntimeException.class, () -> useCase.executar(pedidoId));
    }
}
