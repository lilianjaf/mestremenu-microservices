package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.CriacaoRestauranteNaoAutorizadaException;
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
class ApenasDonoPodeCriarRestauranteRuleTest {

    @InjectMocks
    private ApenasDonoPodeCriarRestauranteRule rule;

    @Mock
    private CriacaoRestauranteContext context;

    @Test
    @DisplayName("Deve validar com sucesso quando o usuário logado for do tipo dono")
    void deveValidarComSucessoQuandoUsuarioLogadoForDono() {
        when(context.isUsuarioLogadoTipoDono()).thenReturn(true);

        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário logado não for do tipo dono")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoForDono() {
        when(context.isUsuarioLogadoTipoDono()).thenReturn(false);

        assertThrows(CriacaoRestauranteNaoAutorizadaException.class, () -> rule.validar(context));
    }
}
