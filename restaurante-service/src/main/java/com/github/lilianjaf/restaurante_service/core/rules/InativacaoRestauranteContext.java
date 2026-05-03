package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

import java.util.Objects;

public record InativacaoRestauranteContext(
        Usuario usuarioLogado,
        Restaurante restaurante
) {
    public boolean isUsuarioDonoDoRestaurante() {
        return usuarioLogado != null &&
               usuarioLogado.isDono() &&
               restaurante != null &&
               Objects.equals(usuarioLogado.getId(), restaurante.getIdDono());
    }

    public boolean isRestauranteAtivo() {
        return restaurante != null && restaurante.isAtivo();
    }
}
