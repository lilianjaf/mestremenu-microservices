package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.DeletarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.DelecaoCardapioNaoAutorizadaException;
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
class ApenasCardapioDoProprioRestaurantePodeSerDeletadoRuleTest {

    @InjectMocks
    private ApenasCardapioDoProprioRestaurantePodeSerDeletadoRule rule;

    @Mock
    private DeletarCardapioRuleContextDto context;

    @Test
    @DisplayName("Deve validar quando o cardápio pertence ao restaurante do usuário")
    void deveValidarQuandoCardapioEDoProprioRestaurante() {
        when(context.isCardapioDoProprioRestaurante()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o cardápio não pertence ao restaurante do usuário")
    void deveLancarExcecaoQuandoCardapioNaoEDoProprioRestaurante() {
        when(context.isCardapioDoProprioRestaurante()).thenReturn(false);
        assertThrows(DelecaoCardapioNaoAutorizadaException.class, () -> rule.validar(context));
    }
}
