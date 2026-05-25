package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.CriacaoItemNaoAutorizadaException;
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
class ApenasDonoPodeCriarItemParaProprioRestauranteRuleTest {

    @InjectMocks
    private ApenasDonoPodeCriarItemParaProprioRestauranteRule rule;

    @Mock
    private ItemCardapioRuleContext context;

    @Test
    @DisplayName("Deve validar quando o usuário é dono do restaurante do item")
    void deveValidarQuandoEDono() {
        when(context.isUsuarioDonoDoRestauranteDoItem()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não é dono do restaurante")
    void deveLancarExcecaoQuandoNaoEDono() {
        when(context.isUsuarioDonoDoRestauranteDoItem()).thenReturn(false);
        assertThrows(CriacaoItemNaoAutorizadaException.class, () -> rule.validar(context));
    }
}
