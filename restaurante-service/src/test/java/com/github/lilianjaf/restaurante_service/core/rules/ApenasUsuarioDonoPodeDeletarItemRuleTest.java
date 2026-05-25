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
class ApenasUsuarioDonoPodeDeletarItemRuleTest {

    @InjectMocks
    private ApenasUsuarioDonoPodeDeletarItemRule rule;

    @Mock
    private DeletarItemCardapioRuleContextDto context;

    @Test
    @DisplayName("Deve validar quando o usuário é DONO e é dono do restaurante")
    void deveValidarQuandoDonoDoRestaurante() {
        when(context.isUsuarioTipoDono()).thenReturn(true);
        when(context.isUsuarioDonoDoRestaurante()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não é do tipo DONO")
    void deveLancarExcecaoQuandoNaoEDono() {
        when(context.isUsuarioTipoDono()).thenReturn(false);
        assertThrows(DelecaoItemNaoAutorizadaException.class, () -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário é DONO mas não é dono do restaurante")
    void deveLancarExcecaoQuandoDonoMasNaoDoRestaurante() {
        when(context.isUsuarioTipoDono()).thenReturn(true);
        when(context.isUsuarioDonoDoRestaurante()).thenReturn(false);
        assertThrows(DelecaoItemNaoAutorizadaException.class, () -> rule.validar(context));
    }
}
