package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;

public record BuscarCardapioPorRestauranteRuleContextDto(
    Usuario usuarioLogado
) {
    public boolean isUsuarioAutenticado() {
        return this.usuarioLogado != null;
    }
}
