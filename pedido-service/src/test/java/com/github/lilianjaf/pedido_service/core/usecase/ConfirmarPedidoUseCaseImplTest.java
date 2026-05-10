package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import com.github.lilianjaf.pedido_service.core.exception.PedidoNaoEncontradoException;
import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import com.github.lilianjaf.pedido_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.pedido_service.core.gateway.OutboxGateway;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.pedido_service.core.rules.ConfirmarPedidoContext;
import com.github.lilianjaf.pedido_service.core.rules.ConfirmarPedidoRule;
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
class ConfirmarPedidoUseCaseImplTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private OutboxGateway outboxGateway;
    @Mock private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    @Mock private TransactionGateway transactionGateway;
    @Mock private ConfirmarPedidoRule permissaoRule;
    @Mock private ConfirmarPedidoRule businessRule;

    private ConfirmarPedidoUseCaseImpl useCase;

    private final UUID clienteId = UUID.randomUUID();
    private final UUID pedidoId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        lenient().when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        useCase = new ConfirmarPedidoUseCaseImpl(
                pedidoRepository, outboxGateway, obterUsuarioLogadoGateway, transactionGateway,
                List.of(permissaoRule), List.of(businessRule));
    }

    private Pedido pedidoEmRascunho() {
        return new Pedido(pedidoId, clienteId, UUID.randomUUID(),
                List.of(new ItemPedido("Lanche", 1, BigDecimal.TEN)),
                BigDecimal.TEN, StatusPedido.RASCUNHO, LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve confirmar pedido com sucesso e emitir evento de saída")
    void deveConfirmarPedidoComSucesso() {
        Pedido pedido = pedidoEmRascunho();
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(new Usuario(clienteId)));
        when(pedidoRepository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.salvar(any())).thenReturn(pedido);

        Pedido resultado = useCase.executar(pedidoId);

        assertNotNull(resultado);
        assertEquals(StatusPedido.CRIADO, pedido.getStatus());
        verify(outboxGateway).salvarEventoPedidoCriado(any());
        verify(permissaoRule).validar(any(ConfirmarPedidoContext.class));
        verify(businessRule).validar(any(ConfirmarPedidoContext.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não está autenticado")
    void deveLancarExcecaoQuandoUsuarioNaoAutenticado() {
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoAutenticadoException.class, () -> useCase.executar(pedidoId));
        verify(pedidoRepository, never()).salvar(any());
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
        when(pedidoRepository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedidoEmRascunho()));
        doThrow(new RuntimeException("Acesso negado")).when(permissaoRule).validar(any());

        assertThrows(RuntimeException.class, () -> useCase.executar(pedidoId));
        verify(pedidoRepository, never()).salvar(any());
    }
}
