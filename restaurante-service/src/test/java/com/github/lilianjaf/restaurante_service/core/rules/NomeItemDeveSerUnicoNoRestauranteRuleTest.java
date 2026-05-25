package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.NomeItemJaEmUsoException;
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
class NomeItemDeveSerUnicoNoRestauranteRuleTest {

    @InjectMocks
    private NomeItemDeveSerUnicoNoRestauranteRule rule;

    @Mock
    private ItemCardapioRuleContext context;

    @Test
    @DisplayName("Deve validar quando o nome do item é único no restaurante")
    void deveValidarQuandoNomeUnico() {
        when(context.isNomeUnico()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o nome do item já está em uso")
    void deveLancarExcecaoQuandoNomeDuplicado() {
        when(context.isNomeUnico()).thenReturn(false);
        assertThrows(NomeItemJaEmUsoException.class, () -> rule.validar(context));
    }
}
