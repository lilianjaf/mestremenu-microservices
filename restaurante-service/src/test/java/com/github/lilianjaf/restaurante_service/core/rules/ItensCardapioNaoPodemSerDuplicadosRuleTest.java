package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.exception.ItensCardapioDuplicadosException;
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
class ItensCardapioNaoPodemSerDuplicadosRuleTest {

    @InjectMocks
    private ItensCardapioNaoPodemSerDuplicadosRule rule;

    @Mock
    private CardapioRuleContext context;

    @Test
    @DisplayName("Deve validar quando não há itens duplicados")
    void deveValidarQuandoSemDuplicados() {
        when(context.hasItensDuplicados()).thenReturn(false);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando há itens duplicados")
    void deveLancarExcecaoQuandoHaDuplicados() {
        when(context.hasItensDuplicados()).thenReturn(true);
        assertThrows(ItensCardapioDuplicadosException.class, () -> rule.validar(context));
    }
}
