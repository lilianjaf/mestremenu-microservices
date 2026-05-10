package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.ItemPedido;
import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.Usuario;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoPedido;
import com.github.lilianjaf.pedido_service.core.dto.DadosCriacaoItemPedido;
import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;
import com.github.lilianjaf.pedido_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.pedido_service.core.gateway.PedidoRepository;
import com.github.lilianjaf.pedido_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.pedido_service.core.rules.CriarPedidoContext;
import com.github.lilianjaf.pedido_service.core.rules.CriarPedidoRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarPedidoUseCaseImplTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;
    @Mock private TransactionGateway transactionGateway;
    @Mock private CriarPedidoRule permissaoRule;
    @Mock private CriarPedidoRule businessRule;

    private CriarPedidoUseCaseImpl useCase;

    private final UUID clienteId = UUID.randomUUID();
    private final UUID restauranteId = UUID.randomUUID();
    private final DadosCriacaoItemPedido itemDto = new DadosCriacaoItemPedido("Lanche", 1, new BigDecimal("25.00"));
    private final DadosCriacaoPedido dados = new DadosCriacaoPedido(restauranteId, List.of(itemDto));

    @BeforeEach
    void setUp() {
        lenient().when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        useCase = new CriarPedidoUseCaseImpl(
                pedidoRepository, obterUsuarioLogadoGateway, transactionGateway,
                List.of(permissaoRule), List.of(businessRule));
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso quando usuário autenticado e dados válidos")
    void deveCriarPedidoComSucesso() {
        Usuario usuario = new Usuario(clienteId);
        Pedido pedidoSalvo = new Pedido(clienteId, restauranteId,
                List.of(new ItemPedido("Lanche", 1, new BigDecimal("25.00"))));

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuario));
        when(pedidoRepository.salvar(any())).thenReturn(pedidoSalvo);

        Pedido resultado = useCase.executar(dados);

        assertNotNull(resultado);
        verify(permissaoRule).validar(any(CriarPedidoContext.class));
        verify(businessRule).validar(any(CriarPedidoContext.class));
        verify(pedidoRepository).salvar(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não está autenticado")
    void deveLancarExcecaoQuandoUsuarioNaoAutenticado() {
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoAutenticadoException.class, () -> useCase.executar(dados));
        verify(pedidoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando regra de permissão é violada")
    void deveLancarExcecaoQuandoRegraDePermissaoViolada() {
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(new Usuario(clienteId)));
        doThrow(new RuntimeException("Permissão negada")).when(permissaoRule).validar(any());

        assertThrows(RuntimeException.class, () -> useCase.executar(dados));
        verify(pedidoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando regra de negócio é violada")
    void deveLancarExcecaoQuandoRegraDeNegocioViolada() {
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(new Usuario(clienteId)));
        doThrow(new RuntimeException("Negócio inválido")).when(businessRule).validar(any());

        assertThrows(RuntimeException.class, () -> useCase.executar(dados));
        verify(pedidoRepository, never()).salvar(any());
    }
}
