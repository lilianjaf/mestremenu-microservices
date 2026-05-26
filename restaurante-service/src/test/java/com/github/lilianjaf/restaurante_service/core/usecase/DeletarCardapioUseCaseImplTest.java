package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.TipoNativo;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.DeletarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.RegistroNaoEncontradoException;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarCardapioUseCaseImplTest {

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private ValidadorPermissaoCardapioRule<DeletarCardapioRuleContextDto> permissaoRule;

    @Mock
    private ValidadorCardapioRule<DeletarCardapioRuleContextDto> businessRule;

    @Mock
    private TransactionGateway transactionGateway;

    private DeletarCardapioUseCaseImpl deletarCardapioUseCase;

    @BeforeEach
    void setUp() {
        deletarCardapioUseCase = new DeletarCardapioUseCaseImpl(
                cardapioRepository,
                restauranteGateway,
                obterUsuarioLogadoGateway,
                transactionGateway,
                List.of(permissaoRule),
                List.of(businessRule)
        );
    }

    @Test
    @DisplayName("Deve deletar um cardápio com sucesso")
    void deveDeletarCardapioComSucesso() {
        UUID idCardapio = UUID.randomUUID();
        UUID idRestaurante = UUID.randomUUID();
        UUID idDono = UUID.randomUUID();
        Cardapio cardapio = new Cardapio("Menu", idRestaurante, Collections.emptyList());
        Restaurante restaurante = new Restaurante(idRestaurante, idDono);
        Usuario usuarioLogado = new Usuario(idDono, TipoNativo.DONO);

        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.of(cardapio));
        when(restauranteGateway.buscarPorId(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(transactionGateway).execute(any(Runnable.class));

        deletarCardapioUseCase.executar(idCardapio);

        verify(permissaoRule).validar(any());
        verify(businessRule).validar(any());
        verify(cardapioRepository).deletar(idCardapio);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cardápio não for encontrado para exclusão")
    void deveLancarExcecaoQuandoCardapioNaoForEncontradoParaExclusao() {
        UUID idCardapio = UUID.randomUUID();

        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> deletarCardapioUseCase.executar(idCardapio));

        verify(cardapioRepository, never()).deletar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não for encontrado ao deletar cardápio")
    void deveLancarExcecaoQuandoRestauranteNaoForEncontradoAoDeletarCardapio() {
        UUID idCardapio = UUID.randomUUID();
        UUID idRestaurante = UUID.randomUUID();
        Cardapio cardapio = new Cardapio("Menu", idRestaurante, Collections.emptyList());

        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.of(cardapio));
        when(restauranteGateway.buscarPorId(idRestaurante)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> deletarCardapioUseCase.executar(idCardapio));

        verify(cardapioRepository, never()).deletar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não for encontrado ao deletar cardápio")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoForEncontradoAoDeletarCardapio() {
        UUID idCardapio = UUID.randomUUID();
        UUID idRestaurante = UUID.randomUUID();
        Cardapio cardapio = new Cardapio("Menu", idRestaurante, Collections.emptyList());
        Restaurante restaurante = new Restaurante(idRestaurante, UUID.randomUUID());

        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.of(cardapio));
        when(restauranteGateway.buscarPorId(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> deletarCardapioUseCase.executar(idCardapio));

        verify(cardapioRepository, never()).deletar(any());
    }
}
