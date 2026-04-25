package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.restaurante.core.dto.DadosAtualizacaoRestaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.DomainException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.ObterUsuarioLogadoRestauranteGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.RestauranteRepository;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.TransactionGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.AtualizarRestauranteRule;
import com.github.lilianjaf.mestremenuclean.restaurante.core.rules.AtualizarRestauranteRuleContextDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarRestauranteUseCaseImplTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ObterUsuarioLogadoRestauranteGateway obterUsuarioLogadoRestauranteGateway;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private AtualizarRestauranteRule permissaoRule;

    @Mock
    private AtualizarRestauranteRule regraDeNegocio;

    private AtualizarRestauranteUseCaseImpl atualizarRestauranteUseCase;

    @BeforeEach
    void setUp() {
        lenient().when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        atualizarRestauranteUseCase = new AtualizarRestauranteUseCaseImpl(
                restauranteRepository,
                obterUsuarioLogadoRestauranteGateway,
                transactionGateway,
                List.of(permissaoRule),
                List.of(regraDeNegocio)
        );
    }

    @Test
    @DisplayName("Deve atualizar um restaurante com sucesso quando os dados forem válidos")
    void deveAtualizarRestauranteComSucesso() {
        UUID id = UUID.randomUUID();
        Usuario usuarioLogado = mock(Usuario.class);
        Restaurante restaurante = mock(Restaurante.class);
        DadosAtualizacaoRestaurante dados = mock(DadosAtualizacaoRestaurante.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante));
        when(restauranteRepository.salvar(any(Restaurante.class))).thenReturn(restaurante);

        atualizarRestauranteUseCase.executar(id, dados);

        verify(permissaoRule).validar(any(AtualizarRestauranteRuleContextDto.class));
        verify(regraDeNegocio).validar(any(AtualizarRestauranteRuleContextDto.class));
        verify(restaurante).atualizar(any(), any(), any(), any());
        verify(restauranteRepository).salvar(restaurante);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário logado não for encontrado")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoEncontrado() {
        UUID id = UUID.randomUUID();
        DadosAtualizacaoRestaurante dados = mock(DadosAtualizacaoRestaurante.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> atualizarRestauranteUseCase.executar(id, dados));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o restaurante não for encontrado")
    void deveLancarExcecaoQuandoRestauranteNaoEncontrado() {
        UUID id = UUID.randomUUID();
        Usuario usuarioLogado = mock(Usuario.class);
        DadosAtualizacaoRestaurante dados = mock(DadosAtualizacaoRestaurante.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> atualizarRestauranteUseCase.executar(id, dados));
    }

    @Test
    @DisplayName("Deve lançar exceção quando uma regra de permissão for violada")
    void deveLancarExcecaoQuandoRegraDePermissaoForViolada() {
        UUID id = UUID.randomUUID();
        Usuario usuarioLogado = mock(Usuario.class);
        Restaurante restaurante = mock(Restaurante.class);
        DadosAtualizacaoRestaurante dados = mock(DadosAtualizacaoRestaurante.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante));
        doThrow(new RuntimeException("Permissão negada")).when(permissaoRule).validar(any(AtualizarRestauranteRuleContextDto.class));

        assertThrows(RuntimeException.class, () -> atualizarRestauranteUseCase.executar(id, dados));
    }

    @Test
    @DisplayName("Deve lançar exceção quando uma regra de negócio for violada")
    void deveLancarExcecaoQuandoRegraDeNegocioForViolada() {
        UUID id = UUID.randomUUID();
        Usuario usuarioLogado = mock(Usuario.class);
        Restaurante restaurante = mock(Restaurante.class);
        DadosAtualizacaoRestaurante dados = mock(DadosAtualizacaoRestaurante.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteRepository.findById(id)).thenReturn(Optional.of(restaurante));
        doThrow(new RuntimeException("Regra de negócio violada")).when(regraDeNegocio).validar(any(AtualizarRestauranteRuleContextDto.class));

        assertThrows(RuntimeException.class, () -> atualizarRestauranteUseCase.executar(id, dados));
    }
}
