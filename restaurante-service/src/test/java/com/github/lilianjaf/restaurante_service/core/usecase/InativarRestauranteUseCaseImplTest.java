package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.exception.DomainException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteRepository;
import com.github.lilianjaf.restaurante_service.core.gateway.TransactionGateway;
import com.github.lilianjaf.restaurante_service.core.rules.InativacaoRestauranteContext;
import com.github.lilianjaf.restaurante_service.core.rules.InativarRestauranteRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InativarRestauranteUseCaseImplTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private InativarRestauranteRule permissaoRule;

    @Mock
    private InativarRestauranteRule regraDeNegocio;

    private InativarRestauranteUseCaseImpl inativarRestauranteUseCase;

    @BeforeEach
    void setUp() {
        lenient().doAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return null;
        }).when(transactionGateway).execute(any(Runnable.class));

        inativarRestauranteUseCase = new InativarRestauranteUseCaseImpl(
                restauranteRepository,
                obterUsuarioLogadoRestauranteGateway,
                transactionGateway,
                List.of(permissaoRule),
                List.of(regraDeNegocio)
        );
    }

    @Test
    @DisplayName("Deve inativar um restaurante com sucesso quando os dados forem válidos")
    void deveInativarRestauranteComSucesso() {
        UUID id = UUID.randomUUID();
        Usuario usuarioLogado = mock(Usuario.class);
        Restaurante restaurante = mock(Restaurante.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante));

        inativarRestauranteUseCase.executar(id);

        verify(permissaoRule).validar(any(InativacaoRestauranteContext.class));
        verify(regraDeNegocio).validar(any(InativacaoRestauranteContext.class));
        verify(restaurante).inativar();
        verify(restauranteRepository).salvar(restaurante);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID do restaurante for nulo")
    void deveLancarExcecaoQuandoIdForNulo() {
        assertThrows(DomainException.class, () -> inativarRestauranteUseCase.executar(null));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário logado não for encontrado")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoEncontrado() {
        UUID id = UUID.randomUUID();

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> inativarRestauranteUseCase.executar(id));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o restaurante não for encontrado")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        UUID id = UUID.randomUUID();
        Usuario usuarioLogado = mock(Usuario.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> inativarRestauranteUseCase.executar(id));
    }

    @Test
    @DisplayName("Deve lançar exceção quando uma regra de permissão for violada")
    void deveLancarExcecaoQuandoRegraDePermissaoForViolada() {
        UUID id = UUID.randomUUID();
        Usuario usuarioLogado = mock(Usuario.class);
        Restaurante restaurante = mock(Restaurante.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante));
        doThrow(new RuntimeException("Permissão negada")).when(permissaoRule).validar(any(InativacaoRestauranteContext.class));

        assertThrows(RuntimeException.class, () -> inativarRestauranteUseCase.executar(id));
    }

    @Test
    @DisplayName("Deve lançar exceção quando uma regra de negócio for violada")
    void deveLancarExcecaoQuandoRegraDeNegocioForViolada() {
        UUID id = UUID.randomUUID();
        Usuario usuarioLogado = mock(Usuario.class);
        Restaurante restaurante = mock(Restaurante.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante));
        doThrow(new RuntimeException("Regra de negócio violada")).when(regraDeNegocio).validar(any(InativacaoRestauranteContext.class));

        assertThrows(RuntimeException.class, () -> inativarRestauranteUseCase.executar(id));
    }
}
