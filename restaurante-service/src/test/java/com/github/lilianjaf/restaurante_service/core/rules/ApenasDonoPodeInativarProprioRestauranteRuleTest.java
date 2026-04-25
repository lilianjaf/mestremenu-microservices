package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.InativacaoRestauranteNaoAutorizadaException;
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
class ApenasDonoPodeInativarProprioRestauranteRuleTest {

    @InjectMocks
    private ApenasDonoPodeInativarProprioRestauranteRule rule;

    @Mock
    private InativacaoRestauranteContext context;

    @Test
    @DisplayName("Deve validar com sucesso quando o usuário for o proprietário do restaurante")
    void deveValidarComSucessoQuandoUsuarioForProprietario() {
        when(context.isUsuarioDonoDoRestaurante()).thenReturn(true);

        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário não for o proprietário do restaurante")
    void deveLancarExcecaoQuandoUsuarioNaoForProprietario() {
        when(context.isUsuarioDonoDoRestaurante()).thenReturn(false);

        assertThrows(InativacaoRestauranteNaoAutorizadaException.class, () -> rule.validar(context));
    }
}
