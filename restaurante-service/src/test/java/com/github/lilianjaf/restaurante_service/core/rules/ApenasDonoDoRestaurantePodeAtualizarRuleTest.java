package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.exception.EdicaoRestauranteNaoAutorizadaException;
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
class ApenasDonoDoRestaurantePodeAtualizarRuleTest {

    @InjectMocks
    private ApenasDonoDoRestaurantePodeAtualizarRule rule;

    @Mock
    private AtualizarRestauranteRuleContextDto context;

    @Test
    @DisplayName("Deve validar com sucesso quando o usuário logado for o proprietário do restaurante")
    void deveValidarComSucessoQuandoUsuarioForProprietario() {
        when(context.isUsuarioLogadoProprietarioDoRestaurante()).thenReturn(true);

        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o usuário logado não for o proprietário do restaurante")
    void deveLancarExcecaoQuandoUsuarioNaoForProprietario() {
        when(context.isUsuarioLogadoProprietarioDoRestaurante()).thenReturn(false);

        assertThrows(EdicaoRestauranteNaoAutorizadaException.class, () -> rule.validar(context));
    }
}
