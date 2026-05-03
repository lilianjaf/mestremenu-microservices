package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.domain.Dono;
import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;

import java.util.function.BooleanSupplier;

public record CriacaoUsuarioContext(
        String nome,
        String email,
        String login,
        String senha,
        String nomeTipo,
        TipoNativo tipoNativo,
        BooleanSupplier emailJaExiste,
        BooleanSupplier loginJaExiste,
        UsuarioBase usuarioLogado
) {
    public boolean isUsuarioLogadoDono() {
        return usuarioLogado instanceof Dono;
    }

    public boolean isEmailJaCadastrado() {
        return emailJaExiste.getAsBoolean();
    }

    public boolean isLoginJaCadastrado() {
        return loginJaExiste.getAsBoolean();
    }

    public boolean isSenhaInformada() {
        return senha != null && !senha.isBlank();
    }
}
