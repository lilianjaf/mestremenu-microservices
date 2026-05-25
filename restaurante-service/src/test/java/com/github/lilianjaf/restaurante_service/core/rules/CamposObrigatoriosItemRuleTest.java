package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.DadosObrigatoriosItemIncompletosException;
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
class CamposObrigatoriosItemRuleTest {

    @InjectMocks
    private CamposObrigatoriosItemRule rule;

    @Mock
    private ItemCardapioRuleContext context;

    @Test
    @DisplayName("Deve validar quando todos os campos estão preenchidos")
    void deveValidarQuandoTodosCamposPreenchidos() {
        when(context.hasTodosCamposPreenchidos()).thenReturn(true);
        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando campos obrigatórios não estão preenchidos")
    void deveLancarExcecaoQuandoCamposFaltando() {
        when(context.hasTodosCamposPreenchidos()).thenReturn(false);
        assertThrows(DadosObrigatoriosItemIncompletosException.class, () -> rule.validar(context));
    }
}
