package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

public record ListarRestaurantesRuleContextDto(
        Usuario usuarioLogado
) {
}
