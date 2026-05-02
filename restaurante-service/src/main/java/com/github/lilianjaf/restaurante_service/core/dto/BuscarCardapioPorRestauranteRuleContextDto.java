package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

import com.github.lilianjaf.restaurante_service.core.rules.RestauranteContextBase;

public record BuscarCardapioPorRestauranteRuleContextDto(
    Usuario usuarioLogado
) implements RestauranteContextBase {
    public boolean isUsuarioAutenticado() {
        return this.usuarioLogado != null;
    }
}
