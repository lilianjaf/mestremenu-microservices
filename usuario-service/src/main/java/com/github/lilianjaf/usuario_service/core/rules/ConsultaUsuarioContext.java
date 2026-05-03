package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;

public record ConsultaUsuarioContext(UsuarioBase usuarioLogado, UsuarioBase usuarioBuscado) {
    public boolean isUsuarioLogadoDono() {
        return usuarioLogado instanceof Dono;
    }

    public boolean isProprioUsuario() {
        return usuarioLogado != null &&
                usuarioBuscado != null &&
                usuarioLogado.getId().equals(usuarioBuscado.getId());
    }

    public boolean isDonoOuProprioUsuario() {
        return isUsuarioLogadoDono() || isProprioUsuario();
    }

    public boolean isUsuarioBuscadoExistente() {
        return usuarioBuscado != null;
    }
}
