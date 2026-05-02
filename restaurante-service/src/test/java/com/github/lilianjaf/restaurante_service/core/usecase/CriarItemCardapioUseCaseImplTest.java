package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.*;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;
import com.github.lilianjaf.restaurante_service.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.restaurante_service.core.gateway.*;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorItemCardapioRule;
import com.github.lilianjaf.restaurante_service.core.rules.ValidadorPermissaoItemCardapioRule;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarItemCardapioUseCaseImplTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private ValidadorPermissaoItemCardapioRule<ItemCardapioRuleContext> permissionRule;

    @Mock
    private ValidadorItemCardapioRule<ItemCardapioRuleContext> businessRule;

    @Mock
    private TransactionGateway transactionGateway;

    private CriarItemCardapioUseCaseImpl criarItemCardapioUseCase;

    @BeforeEach
    void setUp() {
        criarItemCardapioUseCase = new CriarItemCardapioUseCaseImpl(
                itemCardapioRepository,
                cardapioRepository,
                obterUsuarioLogadoGateway,
                restauranteGateway,
                transactionGateway,
                List.of(permissionRule),
                List.of(businessRule)
        );
    }

    @Test
    @DisplayName("Deve criar um item de cardápio com sucesso")
    void deveCriarItemCardapioComSucesso() {
        UUID idCardapio = UUID.randomUUID();
        UUID idRestaurante = UUID.randomUUID();
        UUID idDono = UUID.randomUUID();
        Usuario usuarioLogado = new Usuario(idDono, TipoNativo.DONO);
        Cardapio cardapio = new Cardapio("Menu", idRestaurante, Collections.emptyList());
        Restaurante restaurante = new Restaurante(idRestaurante, idDono);
        DadosCriacaoItemCardapio dados = new DadosCriacaoItemCardapio("Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", idCardapio);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.of(cardapio));
        when(restauranteGateway.buscarPorId(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(itemCardapioRepository.existeNomeNoCardapio("Burger", idCardapio)).thenReturn(false);
        when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        when(itemCardapioRepository.salvar(any(ItemCardapio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemCardapio resultado = criarItemCardapioUseCase.executar(dados);

        assertNotNull(resultado);
        assertEquals("Burger", resultado.getNome());
        assertEquals(idCardapio, resultado.getIdCardapio());
        verify(permissionRule).validar(any());
        verify(businessRule).validar(any());
        verify(itemCardapioRepository).salvar(any(ItemCardapio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando cardápio não for encontrado ao criar item")
    void deveLancarExcecaoQuandoCardapioNaoForEncontradoAoCriarItem() {
        UUID idCardapio = UUID.randomUUID();
        Usuario usuarioLogado = new Usuario(UUID.randomUUID(), TipoNativo.DONO);
        DadosCriacaoItemCardapio dados = new DadosCriacaoItemCardapio("Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", idCardapio);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.empty());

        assertThrows(CardapioException.class, () -> criarItemCardapioUseCase.executar(dados));

        verifyNoInteractions(restauranteGateway);
        verifyNoInteractions(itemCardapioRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não for encontrado ao criar item")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoForEncontradoAoCriarItem() {
        DadosCriacaoItemCardapio dados = new DadosCriacaoItemCardapio("Burger", "Com queijo", BigDecimal.valueOf(25), true, "burger.jpg", UUID.randomUUID());

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> criarItemCardapioUseCase.executar(dados));

        verifyNoInteractions(cardapioRepository);
        verifyNoInteractions(restauranteGateway);
        verifyNoInteractions(itemCardapioRepository);
    }
}
