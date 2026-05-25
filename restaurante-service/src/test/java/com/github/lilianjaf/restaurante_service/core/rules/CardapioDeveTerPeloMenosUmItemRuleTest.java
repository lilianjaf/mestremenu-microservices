package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.exception.CardapioSemItensException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardapioDeveTerPeloMenosUmItemRuleTest {

    @InjectMocks
    private CardapioDeveTerPeloMenosUmItemRule rule;

    @Mock
    private CardapioRuleContext context;

    @Test
    @DisplayName("Deve validar quando itens foram alterados e há pelo menos um")
    void deveValidarQuandoAlterouItensComPeloMenosUm() {
        when(context.alterouItens()).thenReturn(true);
        when(context.hasPeloMenosUmItem()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve validar quando itens não foram alterados (mesmo que esteja vazio)")
    void deveValidarQuandoNaoAlterouItens() {
        when(context.alterouItens()).thenReturn(false);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando itens foram alterados mas a lista ficou vazia")
    void deveLancarExcecaoQuandoAlterouItensParaListaVazia() {
        when(context.alterouItens()).thenReturn(true);
        when(context.hasPeloMenosUmItem()).thenReturn(false);
        assertThrows(CardapioSemItensException.class, () -> rule.validar(context));
    }
}
