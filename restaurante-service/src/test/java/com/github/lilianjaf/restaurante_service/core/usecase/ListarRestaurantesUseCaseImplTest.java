package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.restaurante_service.core.gateway.RestauranteRepository;
import com.github.lilianjaf.restaurante_service.core.rules.ListarRestaurantesRule;
import com.github.lilianjaf.restaurante_service.core.rules.ListarRestaurantesRuleContextDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarRestaurantesUseCaseImplTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoRestauranteGateway;

    @Mock
    private ListarRestaurantesRule permissaoRule;

    @Mock
    private ListarRestaurantesRule regraDeNegocio;

    private ListarRestaurantesUseCaseImpl listarRestaurantesUseCase;

    @BeforeEach
    void setUp() {
        listarRestaurantesUseCase = new ListarRestaurantesUseCaseImpl(
                restauranteRepository,
                obterUsuarioLogadoRestauranteGateway,
                List.of(permissaoRule),
                List.of(regraDeNegocio)
        );
    }

    @Test
    @DisplayName("Deve listar todos os restaurantes com sucesso quando os dados forem válidos")
    void deveListarRestaurantesComSucesso() {
        Usuario usuarioLogado = mock(Usuario.class);
        List<Restaurante> restaurantes = List.of(mock(Restaurante.class));

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteRepository.buscarTodos()).thenReturn(restaurantes);

        List<Restaurante> resultado = listarRestaurantesUseCase.executar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(permissaoRule).validar(any(ListarRestaurantesRuleContextDto.class));
        verify(regraDeNegocio).validar(any(ListarRestaurantesRuleContextDto.class));
        verify(restauranteRepository).buscarTodos();
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário logado não for encontrado")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoEncontrado() {
        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> listarRestaurantesUseCase.executar());

        verify(restauranteRepository, never()).buscarTodos();
        verify(permissaoRule, never()).validar(any());
        verify(regraDeNegocio, never()).validar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando uma regra de permissão for violada")
    void deveLancarExcecaoQuandoRegraDePermissaoForViolada() {
        Usuario usuarioLogado = mock(Usuario.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        doThrow(new RuntimeException("Permissão negada")).when(permissaoRule).validar(any(ListarRestaurantesRuleContextDto.class));

        assertThrows(RuntimeException.class, () -> listarRestaurantesUseCase.executar());

        verify(restauranteRepository, never()).buscarTodos();
    }

    @Test
    @DisplayName("Deve lançar exceção quando uma regra de negócio for violada")
    void deveLancarExcecaoQuandoRegraDeNegocioForViolada() {
        Usuario usuarioLogado = mock(Usuario.class);

        when(obterUsuarioLogadoRestauranteGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        doThrow(new RuntimeException("Regra de negócio violada")).when(regraDeNegocio).validar(any(ListarRestaurantesRuleContextDto.class));

        assertThrows(RuntimeException.class, () -> listarRestaurantesUseCase.executar());

        verify(restauranteRepository, never()).buscarTodos();
    }
}
