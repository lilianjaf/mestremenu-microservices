package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.PrecoItemInvalidoException;
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
class PrecoItemDeveSerMaiorQueZeroRuleTest {

    @InjectMocks
    private PrecoItemDeveSerMaiorQueZeroRule rule;

    @Mock
    private ItemCardapioRuleContext context;

    @Test
    @DisplayName("Deve validar quando o preço é maior que zero")
    void deveValidarQuandoPrecoValido() {
        when(context.isPrecoValido()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o preço é zero ou negativo")
    void deveLancarExcecaoQuandoPrecoInvalido() {
        when(context.isPrecoValido()).thenReturn(false);
        assertThrows(PrecoItemInvalidoException.class, () -> rule.validar(context));
    }
}
