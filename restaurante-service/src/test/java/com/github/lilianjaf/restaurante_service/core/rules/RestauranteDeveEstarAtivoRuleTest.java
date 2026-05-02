package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.exception.RestauranteJaInativoException;
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
class RestauranteDeveEstarAtivoRuleTest {

    @InjectMocks
    private RestauranteDeveEstarAtivoRule rule;

    @Mock
    private InativacaoRestauranteContext context;

    @Test
    @DisplayName("Deve validar com sucesso quando o restaurante estiver ativo")
    void deveValidarComSucessoQuandoRestauranteEstiverAtivo() {
        when(context.isRestauranteAtivo()).thenReturn(true);

        assertDoesNotThrow(() -> rule.validar(context));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o restaurante estiver inativo")
    void deveLancarExcecaoQuandoRestauranteEstiverInativo() {
        when(context.isRestauranteAtivo()).thenReturn(false);

        assertThrows(RestauranteJaInativoException.class, () -> rule.validar(context));
    }
}
