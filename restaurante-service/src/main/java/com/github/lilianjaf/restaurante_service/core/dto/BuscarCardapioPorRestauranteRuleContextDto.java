package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

public record BuscarCardapioPorRestauranteRuleContextDto(
    Usuario usuarioLogado
) {
}
