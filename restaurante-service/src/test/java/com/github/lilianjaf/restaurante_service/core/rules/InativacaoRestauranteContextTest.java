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
class InativacaoRestauranteContextTest {

    @Mock
    private Usuario usuarioLogado;

    @Mock
    private Restaurante restaurante;

    @Test
    @DisplayName("Deve retornar verdadeiro quando o usuário logado for o dono do restaurante")
    void deveRetornarVerdadeiroQuandoUsuarioForDonoDoRestaurante() {
        UUID id = UUID.randomUUID();
        when(usuarioLogado.isDono()).thenReturn(true);
        when(usuarioLogado.getId()).thenReturn(id);
        when(restaurante.getIdDono()).thenReturn(id);
        InativacaoRestauranteContext context = new InativacaoRestauranteContext(usuarioLogado, restaurante);

        assertTrue(context.isUsuarioDonoDoRestaurante());
    }

    @Test
    @DisplayName("Deve retornar falso quando o usuário logado não for dono (perfil)")
    void deveRetornarFalsoQuandoUsuarioNaoForDonoPerfil() {
        when(usuarioLogado.isDono()).thenReturn(false);
        InativacaoRestauranteContext context = new InativacaoRestauranteContext(usuarioLogado, restaurante);

        assertFalse(context.isUsuarioDonoDoRestaurante());
    }

    @Test
    @DisplayName("Deve retornar falso quando o usuário logado não for o dono do restaurante específico")
    void deveRetornarFalsoQuandoUsuarioNaoForDonoDaqueleRestaurante() {
        when(usuarioLogado.isDono()).thenReturn(true);
        when(usuarioLogado.getId()).thenReturn(UUID.randomUUID());
        when(restaurante.getIdDono()).thenReturn(UUID.randomUUID());
        InativacaoRestauranteContext context = new InativacaoRestauranteContext(usuarioLogado, restaurante);

        assertFalse(context.isUsuarioDonoDoRestaurante());
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando o restaurante estiver ativo")
    void deveRetornarVerdadeiroQuandoRestauranteEstiverAtivo() {
        when(restaurante.isAtivo()).thenReturn(true);
        InativacaoRestauranteContext context = new InativacaoRestauranteContext(usuarioLogado, restaurante);

        assertTrue(context.isRestauranteAtivo());
    }

    @Test
    @DisplayName("Deve retornar falso quando o restaurante estiver inativo")
    void deveRetornarFalsoQuandoRestauranteEstiverInativo() {
        when(restaurante.isAtivo()).thenReturn(false);
        InativacaoRestauranteContext context = new InativacaoRestauranteContext(usuarioLogado, restaurante);

        assertFalse(context.isRestauranteAtivo());
    }
}
