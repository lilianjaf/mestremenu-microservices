package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;

public record ListarRestaurantesRuleContextDto(
        Usuario usuarioLogado
) implements RestauranteContextBase {
    public boolean isUsuarioAutenticado() {
        return this.usuarioLogado != null;
    }
}
