package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.RestauranteNaoVinculadoAoItemException;
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
class ItemDeveTerRestauranteVinculadoRuleTest {

    @InjectMocks
    private ItemDeveTerRestauranteVinculadoRule rule;

    @Mock
    private ItemCardapioRuleContext context;

    @Test
    @DisplayName("Deve validar quando o restaurante está vinculado")
    void deveValidarQuandoRestauranteVinculado() {
        when(context.hasRestauranteVinculado()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o restaurante não está vinculado")
    void deveLancarExcecaoQuandoRestauranteNaoVinculado() {
        when(context.hasRestauranteVinculado()).thenReturn(false);
        assertThrows(RestauranteNaoVinculadoAoItemException.class, () -> rule.validar(context));
    }
}
