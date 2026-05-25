package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.DeletarItemCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.DelecaoItemNaoAutorizadaException;
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
class ApenasDonoDoRestaurantePodeDeletarItemRuleTest {

    @InjectMocks
    private ApenasDonoDoRestaurantePodeDeletarItemRule rule;

    @Mock
    private DeletarItemCardapioRuleContextDto context;

    @Test
    @DisplayName("Deve validar quando o item pertence ao restaurante do usuário")
    void deveValidarQuandoItemEDoProprioRestaurante() {
        when(context.isItemDoProprioRestaurante()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o item não pertence ao restaurante do usuário")
    void deveLancarExcecaoQuandoItemNaoEDoProprioRestaurante() {
        when(context.isItemDoProprioRestaurante()).thenReturn(false);
        assertThrows(DelecaoItemNaoAutorizadaException.class, () -> rule.validar(context));
    }
}
