package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;

public record CriacaoTipoUsuarioContext(String nome, boolean existeComMesmoNome, UsuarioBase usuarioLogado) {
    public boolean isUsuarioLogadoAutenticado() {
        return usuarioLogado != null;
    }

    public boolean isUsuarioLogadoDono() {
        return usuarioLogado instanceof Dono;
    }

    public boolean isNomeJaCadastrado() {
        return existeComMesmoNome;
    }
}
