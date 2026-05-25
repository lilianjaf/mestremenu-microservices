package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.exception.NomeCardapioJaEmUsoException;
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
class NomeCardapioDeveSerUnicoNoRestauranteRuleTest {

    @InjectMocks
    private NomeCardapioDeveSerUnicoNoRestauranteRule rule;

    @Mock
    private CardapioRuleContext context;

    @Test
    @DisplayName("Deve validar quando o nome do cardápio é único")
    void deveValidarQuandoNomeUnico() {
        when(context.isNomeUnico()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome do cardápio já está em uso")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        when(context.isNomeUnico()).thenReturn(false);
        assertThrows(NomeCardapioJaEmUsoException.class, () -> rule.validar(context));
    }
}
