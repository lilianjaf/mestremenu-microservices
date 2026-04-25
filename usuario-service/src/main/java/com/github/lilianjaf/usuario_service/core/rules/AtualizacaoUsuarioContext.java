package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.mestremenuclean.usuario.core.domain.Dono;
import com.github.lilianjaf.mestremenuclean.usuario.core.domain.UsuarioBase;

public record AtualizacaoUsuarioContext(
        UsuarioBase usuarioSendoEditado,
        UsuarioBase usuarioComMesmoEmail,
        UsuarioBase usuarioLogado
) {
    public boolean isUsuarioLogadoAutenticado() {
        return usuarioLogado != null;
    }

    public boolean isConflitoDeEmail() {
        return usuarioComMesmoEmail != null &&
                usuarioSendoEditado != null &&
                !usuarioComMesmoEmail.getId().equals(usuarioSendoEditado.getId());
    }

    public boolean isUsuarioLogadoDono() {
        return usuarioLogado instanceof Dono;
    }

    public boolean isProprioUsuario() {
        return usuarioLogado != null &&
                usuarioSendoEditado != null &&
                usuarioLogado.getId().equals(usuarioSendoEditado.getId());
    }

    public boolean isDonoOuProprioUsuario() {
        return isUsuarioLogadoDono() || isProprioUsuario();
    }

    public boolean isUsuarioSendoEditadoExistente() {
        return usuarioSendoEditado != null;
    }
}
