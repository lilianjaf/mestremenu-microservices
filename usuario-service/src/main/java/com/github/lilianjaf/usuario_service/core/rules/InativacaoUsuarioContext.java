package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;

public record InativacaoUsuarioContext(
        UsuarioBase usuarioLogado,
        UsuarioBase usuarioAlvo
) {
    public boolean isUsuarioLogadoDono() {
        return usuarioLogado instanceof Dono;
    }

    public boolean isUsuarioLogadoProprioUsuarioAlvo() {
        return usuarioLogado.getId().equals(usuarioAlvo.getId());
    }

    public boolean isDonoOuProprioUsuarioAlvo() {
        return isUsuarioLogadoDono() || isUsuarioLogadoProprioUsuarioAlvo();
    }

}
