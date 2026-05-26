package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.*;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoItemCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.RegistroNaoEncontradoException;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlterarItemCardapioUseCaseImplTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private CardapioRepository cardapioRepository;

    @Mock
    private RestauranteGateway restauranteGateway;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private TransactionGateway transactionGateway;

    @Mock
    private ValidadorPermissaoItemCardapioRule<ItemCardapioRuleContext> permissionRule;

    @Mock
    private ValidadorItemCardapioRule<ItemCardapioRuleContext> businessRule;

    private AlterarItemCardapioUseCaseImpl alterarItemCardapioUseCase;

    @BeforeEach
    void setUp() {
        alterarItemCardapioUseCase = new AlterarItemCardapioUseCaseImpl(
                itemCardapioRepository,
                cardapioRepository,
                restauranteGateway,
                obterUsuarioLogadoGateway,
                transactionGateway,
                List.of(permissionRule),
                List.of(businessRule)
        );
    }

    @Test
    @DisplayName("Deve alterar um item de cardápio com sucesso")
    void deveAlterarItemCardapioComSucesso() {
        UUID idItem = UUID.randomUUID();
        UUID idCardapio = UUID.randomUUID();
        UUID idRestaurante = UUID.randomUUID();
        UUID idDono = UUID.randomUUID();

        ItemCardapio item = new ItemCardapio("Burger", "Top", BigDecimal.TEN, true, "img.png", idCardapio);
        Cardapio cardapio = new Cardapio("Menu", idRestaurante, Collections.emptyList());
        Restaurante restaurante = new Restaurante(idRestaurante, idDono);
        Usuario usuarioLogado = new Usuario(idDono, TipoNativo.DONO);

        DadosAtualizacaoItemCardapio dados = new DadosAtualizacaoItemCardapio(
                idItem, "Burger Deluxe", "Descricao", new BigDecimal("35.00"), true, "novo.jpg"
        );

        when(itemCardapioRepository.findById(idItem)).thenReturn(Optional.of(item));
        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.of(cardapio));
        when(restauranteGateway.buscarPorId(idRestaurante)).thenReturn(Optional.of(restaurante));
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        
        when(itemCardapioRepository.existeNomeNoCardapioExcetoId(any(), any(), any())).thenReturn(false);
        
        when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
        
        when(itemCardapioRepository.salvar(any(ItemCardapio.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemCardapio resultado = alterarItemCardapioUseCase.executar(dados);

        assertNotNull(resultado);
        assertEquals(dados.nome(), resultado.getNome());
        assertEquals(dados.preco(), resultado.getPreco());
        verify(permissionRule).validar(any());
        verify(businessRule).validar(any());
        verify(itemCardapioRepository).salvar(any(ItemCardapio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando item não for encontrado")
    void deveLancarExcecaoQuandoItemNaoForEncontrado() {
        UUID idItem = UUID.randomUUID();
        DadosAtualizacaoItemCardapio dados = new DadosAtualizacaoItemCardapio(
                idItem, "Burger Deluxe", "Descricao", new BigDecimal("35.00"), true, "novo.jpg"
        );

        when(itemCardapioRepository.findById(idItem)).thenReturn(Optional.empty());

        assertThrows(RegistroNaoEncontradoException.class, () -> alterarItemCardapioUseCase.executar(dados));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não for encontrado")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoForEncontrado() {
        UUID idItem = UUID.randomUUID();
        UUID idCardapio = UUID.randomUUID();
        ItemCardapio item = new ItemCardapio("Burger", "Top", BigDecimal.TEN, true, "img.png", idCardapio);
        Cardapio cardapio = new Cardapio("Menu", UUID.randomUUID(), Collections.emptyList());

        DadosAtualizacaoItemCardapio dados = new DadosAtualizacaoItemCardapio(
                idItem, "Burger Deluxe", "Descricao", new BigDecimal("35.00"), true, "novo.jpg"
        );

        when(itemCardapioRepository.findById(idItem)).thenReturn(Optional.of(item));
        when(cardapioRepository.findById(idCardapio)).thenReturn(Optional.of(cardapio));
        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> alterarItemCardapioUseCase.executar(dados));
    }
}
