package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.restaurante.core.dto.DadosCriacaoRestaurante;

public record CriacaoRestauranteContext(
        Usuario usuarioLogado,
        Usuario dono,
        DadosCriacaoRestaurante dados
) implements RestauranteContextBase {
    public boolean isUsuarioAutenticado() {
        return usuarioLogado != null;
    }

    public boolean isUsuarioLogadoTipoDono() {
        return usuarioLogado != null && usuarioLogado.isDono();
    }

    public boolean hasDonoVinculado() {
        return dono != null;
    }
}
