package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;

public record BuscarRestauranteRuleContextDto(
        Usuario usuarioLogado,
        Restaurante restaurante
) implements RestauranteContextBase {
    public boolean isUsuarioAutenticado() {
        return this.usuarioLogado != null;
    }
}
