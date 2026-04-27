package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.ItemCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.TipoNativo;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.BuscarItemCardapioPorIdRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CardapioException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.UsuarioLogadoNaoEncontradoException;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.ItemCardapioRepository;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.ObterUsuarioLogadoGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.TransactionGateway;
import com.github.lilianjaf.mestremenuclean.cardapio.core.rules.ValidadorPermissaoCardapioRule;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarItemCardapioPorIdUseCaseImplTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private ObterUsuarioLogadoGateway obterUsuarioLogadoGateway;

    @Mock
    private ValidadorPermissaoCardapioRule<BuscarItemCardapioPorIdRuleContextDto> permissaoRule;

    @Mock
    private TransactionGateway transactionGateway;

    private BuscarItemCardapioPorIdUseCaseImpl buscarItemCardapioPorIdUseCase;

    @BeforeEach
    void setUp() {
        buscarItemCardapioPorIdUseCase = new BuscarItemCardapioPorIdUseCaseImpl(
                itemCardapioRepository,
                obterUsuarioLogadoGateway,
                transactionGateway,
                List.of(permissaoRule)
        );
    }

    @Test
    @DisplayName("Deve buscar item do cardápio por id com sucesso")
    void deveBuscarItemCardapioPorIdComSucesso() {
        UUID idItem = UUID.randomUUID();
        Usuario usuarioLogado = new Usuario(UUID.randomUUID(), TipoNativo.CLIENTE);
        ItemCardapio itemEsperado = new ItemCardapio("Pizza", "De queijo", BigDecimal.TEN, true, "foto.jpg", UUID.randomUUID());

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(itemCardapioRepository.findById(idItem)).thenReturn(Optional.of(itemEsperado));
        when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        ItemCardapio resultado = buscarItemCardapioPorIdUseCase.executar(idItem);

        assertEquals(itemEsperado, resultado);
        verify(permissaoRule).validar(any());
        verify(itemCardapioRepository).findById(idItem);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário logado não for encontrado ao buscar item")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoForEncontradoAoBuscarItem() {
        UUID idItem = UUID.randomUUID();

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.empty());

        assertThrows(UsuarioLogadoNaoEncontradoException.class, () -> buscarItemCardapioPorIdUseCase.executar(idItem));

        verifyNoInteractions(itemCardapioRepository);
        verify(permissaoRule, never()).validar(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando item não for encontrado")
    void deveLancarExcecaoQuandoItemNaoForEncontrado() {
        UUID idItem = UUID.randomUUID();
        Usuario usuarioLogado = new Usuario(UUID.randomUUID(), TipoNativo.CLIENTE);

        when(obterUsuarioLogadoGateway.obterUsuarioLogado()).thenReturn(Optional.of(usuarioLogado));
        when(itemCardapioRepository.findById(idItem)).thenReturn(Optional.empty());
        when(transactionGateway.execute(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });

        assertThrows(CardapioException.class, () -> buscarItemCardapioPorIdUseCase.executar(idItem));

        verify(permissaoRule).validar(any());
        verify(itemCardapioRepository).findById(idItem);
    }
}
