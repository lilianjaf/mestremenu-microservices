package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.TipoNativo;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.CardapioRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorCardapioRule;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorPermissaoCardapioRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
class AlterarCardapioUseCaseImplTest {

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private ValidadorPermissaoCardapioRule<AlterarCardapioRuleContextDto> permissaoRule;

    @Mock
    private ValidadorCardapioRule<AlterarCardapioRuleContextDto> businessRule;

    @Mock
    private TransactionGateway transactionGateway;

    private AlterarCardapioUseCaseImpl alterarCardapioUseCase;

    @BeforeEach
    void setUp() {
        alterarCardapioUseCase = new AlterarCardapioUseCaseImpl(
                cardapioRepository,
                restauranteGateway,
                obterUsuarioLogadoGateway,
                transactionGateway,
                List.of(permissaoRule),
                List.of(businessRule)
        );
    }

    @Test
    @DisplayName("Deve alterar um cardápio com sucesso")
    void deveAlterarCardapioComSucesso() {
        UUID idCardapio = UUID.randomUUID();
        UUID idRestaurante = UUID.randomUUID();
        UUID idDono = UUID.randomUUID();
        Cardapio cardapio = new Cardapio("Menu Antigo", idRestaurante, Collections.emptyList());
        Restaurante restaurante = new Restaurante(idRestaurante, idDono);
        Usuario usuarioLogado = new Usuario(idDono, TipoNativo.DONO);
        
        DadosCriacaoItemCardapio novoItemDto = new DadosCriacaoItemCardapio("Novo Item", "Desc", BigDecimal.TEN, true, "img.jpg", idCardapio);
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(idCardapio, "Menu Novo", List.of(novoItemDto));

        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.of(cardapio));
        when(restauranteGateway.buscarPorId(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(cardapioRepository.existeNomeParaRestaurante("Menu Novo", idRestaurante)).thenReturn(false);
        when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        when(cardapioRepository.salvar(any(Cardapio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cardapio resultado = alterarCardapioUseCase.executar(dados);

        assertEquals("Menu Novo", resultado.getNome());
        assertEquals(1, resultado.getItens().size());
        verify(permissaoRule).validar(any());
        verify(businessRule).validar(any());
        verify(cardapioRepository).salvar(any(Cardapio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando cardápio não for encontrado ao alterar")
    void deveLancarExcecaoQuandoCardapioNaoForEncontradoAoAlterar() {
        UUID idCardapio = UUID.randomUUID();
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(idCardapio, "Novo", List.of());

        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.empty());

        assertThrows(CardapioException.class, () -> alterarCardapioUseCase.executar(dados));

        verify(cardapioRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não for encontrado ao alterar cardápio")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoForEncontradoAoAlterarCardapio() {
        UUID idCardapio = UUID.randomUUID();
        UUID idRestaurante = UUID.randomUUID();
        Cardapio cardapio = new Cardapio("Menu", idRestaurante, Collections.emptyList());
        Restaurante restaurante = new Restaurante(idRestaurante, UUID.randomUUID());
        DadosAtualizacaoCardapio dados = new DadosAtualizacaoCardapio(idCardapio, "Novo", List.of());

        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.of(cardapio));
        when(restauranteGateway.buscarPorId(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> alterarCardapioUseCase.executar(dados));

        verify(cardapioRepository, never()).salvar(any());
    }
}
