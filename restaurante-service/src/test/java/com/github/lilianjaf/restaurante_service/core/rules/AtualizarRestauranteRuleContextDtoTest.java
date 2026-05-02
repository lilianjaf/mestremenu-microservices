package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarRestauranteRuleContextDtoTest {

    @Mock
    private Usuario usuarioLogado;

    @Mock
    private Restaurante restaurante;

    @Test
    @DisplayName("Deve retornar verdadeiro quando o usuário logado for o proprietário")
    void deveRetornarVerdadeiroQuandoUsuarioForProprietario() {
        UUID id = UUID.randomUUID();
        when(usuarioLogado.getId()).thenReturn(id);
        when(restaurante.getIdDono()).thenReturn(id);
        AtualizarRestauranteRuleContextDto context = new AtualizarRestauranteRuleContextDto(usuarioLogado, restaurante);

        assertTrue(context.isUsuarioLogadoProprietarioDoRestaurante());
    }

    @Test
    @DisplayName("Deve retornar falso quando o usuário logado não for o proprietário")
    void deveRetornarFalsoQuandoUsuarioNaoForProprietario() {
        when(usuarioLogado.getId()).thenReturn(UUID.randomUUID());
        when(restaurante.getIdDono()).thenReturn(UUID.randomUUID());
        AtualizarRestauranteRuleContextDto context = new AtualizarRestauranteRuleContextDto(usuarioLogado, restaurante);

        assertFalse(context.isUsuarioLogadoProprietarioDoRestaurante());
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando o restaurante possuir dono")
    void deveRetornarVerdadeiroQuandoRestaurantePossuirDono() {
        when(restaurante.getIdDono()).thenReturn(UUID.randomUUID());
        AtualizarRestauranteRuleContextDto context = new AtualizarRestauranteRuleContextDto(usuarioLogado, restaurante);

        assertTrue(context.restaurantePossuiDono());
    }

    @Test
    @DisplayName("Deve retornar falso quando o restaurante não possuir dono")
    void deveRetornarFalsoQuandoRestauranteNaoPossuirDono() {
        when(restaurante.getIdDono()).thenReturn(null);
        AtualizarRestauranteRuleContextDto context = new AtualizarRestauranteRuleContextDto(usuarioLogado, restaurante);

        assertFalse(context.restaurantePossuiDono());
    }
}
