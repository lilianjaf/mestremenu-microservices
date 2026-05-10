package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.exception.PedidoNaoEncontradoException;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.pedido_service.core.rules.AtualizarStatusPedidoRule;
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
class AtualizarStatusPedidoUseCaseImplTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private TransactionGateway transactionGateway;
    @Mock private AtualizarStatusPedidoRule businessRule;

    private AtualizarStatusPedidoUseCaseImpl useCase;

    private final UUID pedidoId = UUID.randomUUID();
    private final UUID clienteId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        lenient().when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        useCase = new AtualizarStatusPedidoUseCaseImpl(
                pedidoRepository, transactionGateway, List.of(businessRule));
    }

    private Pedido pedidoComStatus(StatusPedido status) {
        return new Pedido(pedidoId, clienteId, UUID.randomUUID(),
                List.of(new ItemPedido("Lanche", 1, BigDecimal.TEN)),
                BigDecimal.TEN, status, LocalDateTime.now());
    }

    @Test
    @DisplayName("Deve atualizar status de CRIADO para PAGO com sucesso")
    void deveAtualizarStatusParaPago() {
        Pedido pedido = pedidoComStatus(StatusPedido.CRIADO);
        when(pedidoRepository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.salvar(any())).thenReturn(pedido);

        Pedido resultado = useCase.executar(pedidoId, StatusPedido.PAGO);

        assertEquals(StatusPedido.PAGO, resultado.getStatus());
        verify(businessRule).validar(any());
        verify(pedidoRepository).salvar(pedido);
    }

    @Test
    @DisplayName("Deve atualizar status de CRIADO para CANCELADO com sucesso")
    void deveAtualizarStatusParaCancelado() {
        Pedido pedido = pedidoComStatus(StatusPedido.CRIADO);
        when(pedidoRepository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.salvar(any())).thenReturn(pedido);

        Pedido resultado = useCase.executar(pedidoId, StatusPedido.CANCELADO);

        assertEquals(StatusPedido.CANCELADO, resultado.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não existe")
    void deveLancarExcecaoQuandoPedidoNaoExiste() {
        when(pedidoRepository.buscarPorId(pedidoId)).thenReturn(Optional.empty());

        assertThrows(PedidoNaoEncontradoException.class,
                () -> useCase.executar(pedidoId, StatusPedido.PAGO));
        verify(pedidoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando regra de negócio é violada")
    void deveLancarExcecaoQuandoRegraDeNegocioViolada() {
        Pedido pedido = pedidoComStatus(StatusPedido.CRIADO);
        when(pedidoRepository.buscarPorId(pedidoId)).thenReturn(Optional.of(pedido));
        doThrow(new RuntimeException("Transição inválida")).when(businessRule).validar(any());

        assertThrows(RuntimeException.class, () -> useCase.executar(pedidoId, StatusPedido.PAGO));
        verify(pedidoRepository, never()).salvar(any());
    }
}
