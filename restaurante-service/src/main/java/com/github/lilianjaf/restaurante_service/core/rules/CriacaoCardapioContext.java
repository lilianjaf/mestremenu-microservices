package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoCardapio;

public record CriacaoCardapioContext(
        Usuario usuarioLogado,
        Restaurante restaurante,
        DadosCriacaoCardapio dados
) {
}
