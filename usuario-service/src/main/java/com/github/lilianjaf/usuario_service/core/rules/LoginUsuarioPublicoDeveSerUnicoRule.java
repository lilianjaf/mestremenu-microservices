package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.mestremenuclean.usuario.core.exception.LoginUsuarioJaEmUsoException;

public class LoginUsuarioPublicoDeveSerUnicoRule implements ValidadorCriacaoUsuarioPublicoRule {
    @Override
    public void validar(CriacaoUsuarioPublicoContext context) {
        if (context.isLoginJaCadastrado()) {
            throw new LoginUsuarioJaEmUsoException("Login já está em uso.");
        }
    }
}
