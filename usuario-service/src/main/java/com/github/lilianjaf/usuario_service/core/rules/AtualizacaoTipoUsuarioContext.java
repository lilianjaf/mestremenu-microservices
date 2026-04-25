package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.mestremenuclean.usuario.core.domain.Dono;
import com.github.lilianjaf.mestremenuclean.usuario.core.domain.TipoUsuario;
import com.github.lilianjaf.mestremenuclean.usuario.core.domain.UsuarioBase;

public record AtualizacaoTipoUsuarioContext(
        TipoUsuario tipoAtual,
        TipoUsuario tipoComMesmoNome,
        UsuarioBase usuarioLogado
) {
    public boolean isUsuarioLogadoAutenticado() {
        return usuarioLogado != null;
    }

    public boolean isUsuarioLogadoDono() {
        return usuarioLogado instanceof Dono;
    }

    public boolean isNomeJaEmUso() {
        return tipoComMesmoNome != null && !tipoComMesmoNome.getId().equals(tipoAtual.getId());
    }

    public String getNomeTipoComMesmoNome() {
        return tipoComMesmoNome != null ? tipoComMesmoNome.getNome() : "";
    }
}
