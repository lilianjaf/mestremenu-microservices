package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoRestaurante;

public record CriacaoRestauranteContext(
        Usuario usuarioLogado,
        Usuario dono,
        DadosCriacaoRestaurante dados
) {
    public boolean isUsuarioLogadoTipoDono() {
        return usuarioLogado != null && usuarioLogado.isDono();
    }

    public boolean hasDonoVinculado() {
        return dono != null;
    }
}
