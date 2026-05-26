package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.TipoNativo;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.CriarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoItemCardapio;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarCardapioUseCaseImplTest {

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private ValidadorPermissaoCardapioRule<CriarCardapioRuleContextDto> permissionRule;

    @Mock
    private ValidadorCardapioRule<CriarCardapioRuleContextDto> businessRule;

    @Mock
    private TransactionGateway transactionGateway;

    private CriarCardapioUseCaseImpl criarCardapioUseCase;

    @BeforeEach
    void setUp() {
        criarCardapioUseCase = new CriarCardapioUseCaseImpl(
                cardapioRepository,
                restauranteGateway,
                obterUsuarioLogadoGateway,
                transactionGateway,
                List.of(permissionRule),
                List.of(businessRule)
        );
    }

    @Test
    @DisplayName("Deve criar um cardápio com sucesso")
    void deveCriarCardapioComSucesso() {
        UUID idRestaurante = UUID.randomUUID();
        UUID idDono = UUID.randomUUID();
        Usuario usuarioLogado = new Usuario(idDono, TipoNativo.DONO);
        Restaurante restaurante = new Restaurante(idRestaurante, idDono);
        
        DadosCriacaoItemCardapio itemDto = new DadosCriacaoItemCardapio("Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", null);
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu Principal", idRestaurante, List.of(itemDto));

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteGateway.buscarPorId(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(cardapioRepository.existeNomeParaRestaurante("Menu Principal", idRestaurante)).thenReturn(false);
        when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        when(cardapioRepository.salvar(any(Cardapio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cardapio resultado = criarCardapioUseCase.executar(dados);

        assertNotNull(resultado);
        assertEquals("Menu Principal", resultado.getNome());
        assertEquals(idRestaurante, resultado.getIdRestaurante());
        assertEquals(1, resultado.getItens().size());
        verify(permissionRule).validar(any());
        verify(businessRule).validar(any());
        verify(cardapioRepository).salvar(any(Cardapio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando restaurante não for encontrado ao criar cardápio")
    void deveLancarExcecaoQuandoRestauranteNaoForEncontradoAoCriarCardapio() {
        UUID idRestaurante = UUID.randomUUID();
        Usuario usuarioLogado = new Usuario(UUID.randomUUID(), TipoNativo.DONO);
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu", idRestaurante, List.of());

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(restauranteGateway.buscarPorId(idRestaurante)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> criarCardapioUseCase.executar(dados));

        verifyNoInteractions(cardapioRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não for encontrado ao criar cardápio")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoForEncontradoAoCriarCardapio() {
        DadosCriacaoCardapio dados = new DadosCriacaoCardapio("Menu", UUID.randomUUID(), List.of());

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> criarCardapioUseCase.executar(dados));

        verifyNoInteractions(restauranteGateway);
        verifyNoInteractions(cardapioRepository);
    }
}
