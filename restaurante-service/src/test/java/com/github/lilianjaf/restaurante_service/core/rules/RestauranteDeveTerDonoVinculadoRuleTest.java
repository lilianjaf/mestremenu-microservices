package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.RestauranteSemDonoException;
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
class RestauranteDeveTerDonoVinculadoRuleTest {

    @InjectMocks
    private RestauranteDeveTerDonoVinculadoRule rule;

    @Mock
    private AtualizarRestauranteRuleContextDto atualizarContext;

    @Mock
    private CriacaoRestauranteContext criacaoContext;

    @Test
    @DisplayName("Deve validar com sucesso na atualização quando o restaurante possuir dono")
    void deveValidarComSucessoNaAtualizacaoQuandoPossuirDono() {
        when(atualizarContext.restaurantePossuiDono()).thenReturn(true);

        assertDoesNotThrow(() -> rule.validar(atualizarContext));
    }

    @Test
    @DisplayName("Deve lançar exceção na atualização quando o restaurante não possuir dono")
    void deveLancarExcecaoNaAtualizacaoQuandoNaoPossuirDono() {
        when(atualizarContext.restaurantePossuiDono()).thenReturn(false);

        assertThrows(RestauranteSemDonoException.class, () -> rule.validar(atualizarContext));
    }

    @Test
    @DisplayName("Deve validar com sucesso na criação quando o restaurante possuir dono")
    void deveValidarComSucessoNaCriacaoQuandoPossuirDono() {
        when(criacaoContext.hasDonoVinculado()).thenReturn(true);

        assertDoesNotThrow(() -> rule.validar(criacaoContext));
    }

    @Test
    @DisplayName("Deve lançar exceção na criação quando o restaurante não possuir dono")
    void deveLancarExcecaoNaCriacaoQuandoNaoPossuirDono() {
        when(criacaoContext.hasDonoVinculado()).thenReturn(false);

        assertThrows(RestauranteSemDonoException.class, () -> rule.validar(criacaoContext));
    }
}
