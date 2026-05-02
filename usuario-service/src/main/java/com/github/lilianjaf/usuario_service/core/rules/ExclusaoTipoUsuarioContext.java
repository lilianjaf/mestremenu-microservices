package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;

import java.util.Optional;
import java.util.function.BooleanSupplier;

public record ExclusaoTipoUsuarioContext(
        Optional<TipoUsuario> tipoUsuarioASerDeletado,
        BooleanSupplier estaEmUso,
        UsuarioBase usuarioLogado
) {
    public boolean isUsuarioLogadoAutenticado() {
        return usuarioLogado != null;
    }

    public boolean isUsuarioLogadoDono() {
        return usuarioLogado instanceof Dono;
    }

    public boolean isTipoUsuarioPresente() {
        return tipoUsuarioASerDeletado.isPresent();
    }

    public boolean isTipoUsuarioEmUso() {
        return estaEmUso.getAsBoolean();
    }

    public String getNomeTipoUsuario() {
        return tipoUsuarioASerDeletado.map(TipoUsuario::getNome).orElse("");
    }

}
