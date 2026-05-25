package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.exception.NomeCardapioObrigatorioException;
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
class CardapioDeveTerNomeRuleTest {

    @InjectMocks
    private CardapioDeveTerNomeRule rule;

    @Mock
    private CardapioRuleContext context;

    @Test
    @DisplayName("Deve validar quando o cardápio tem nome")
    void deveValidarQuandoTemNome() {
        when(context.hasNome()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o cardápio não tem nome")
    void deveLancarExcecaoQuandoSemNome() {
        when(context.hasNome()).thenReturn(false);
        assertThrows(NomeCardapioObrigatorioException.class, () -> rule.validar(context));
    }
}
