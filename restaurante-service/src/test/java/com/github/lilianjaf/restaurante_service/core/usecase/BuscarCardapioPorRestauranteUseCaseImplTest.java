package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Cardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.TipoNativo;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.BuscarCardapioPorRestauranteRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.CardapioRepository;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.TransactionGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.rules.ValidadorPermissaoCardapioRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarCardapioPorRestauranteUseCaseImplTest {

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private ValidadorPermissaoCardapioRule<BuscarCardapioPorRestauranteRuleContextDto> permissaoRule;

    @Mock
    private TransactionGateway transactionGateway;

    private BuscarCardapioPorRestauranteUseCaseImpl buscarCardapioPorRestauranteUseCase;

    @BeforeEach
    void setUp() {
        buscarCardapioPorRestauranteUseCase = new BuscarCardapioPorRestauranteUseCaseImpl(
                cardapioRepository,
                obterUsuarioLogadoGateway,
                transactionGateway,
                List.of(permissaoRule)
        );
    }

    @Test
    @DisplayName("Deve buscar cardápios por restaurante com sucesso")
    void deveBuscarCardapiosPorRestauranteComSucesso() {
        UUID idRestaurante = UUID.randomUUID();
        Usuario usuarioLogado = new Usuario(UUID.randomUUID(), TipoNativo.DONO);
        List<Cardapio> cardapiosEsperados = List.of(new Cardapio("Almoço", idRestaurante, Collections.emptyList()));

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(cardapioRepository.buscarPorIdRestaurante(idRestaurante)).thenReturn(cardapiosEsperados);
        when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        List<Cardapio> resultado = buscarCardapioPorRestauranteUseCase.executar(idRestaurante);

        assertEquals(cardapiosEsperados, resultado);
        verify(permissaoRule).validar(any());
        verify(cardapioRepository).buscarPorIdRestaurante(idRestaurante);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não for encontrado")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoForEncontrado() {
        UUID idRestaurante = UUID.randomUUID();

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> buscarCardapioPorRestauranteUseCase.executar(idRestaurante));

        verifyNoInteractions(cardapioRepository);
        verify(permissaoRule, never()).validar(any());
    }
}
