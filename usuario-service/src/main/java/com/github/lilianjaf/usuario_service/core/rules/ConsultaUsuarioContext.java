package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.mestremenuclean.usuario.core.domain.Dono;
import com.github.lilianjaf.mestremenuclean.usuario.core.domain.UsuarioBase;

public record ConsultaUsuarioContext(UsuarioBase usuarioLogado, UsuarioBase usuarioBuscado) {
    public boolean isUsuarioLogadoAutenticado() {
        return usuarioLogado != null;
    }

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
