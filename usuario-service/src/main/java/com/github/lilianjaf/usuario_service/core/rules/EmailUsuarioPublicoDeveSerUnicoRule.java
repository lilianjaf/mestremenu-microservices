package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.EmailUsuarioJaEmUsoException;

public class EmailUsuarioPublicoDeveSerUnicoRule implements ValidadorCriacaoUsuarioPublicoRule {
    @Override
    public void validar(CriacaoUsuarioPublicoContext context) {
        if (context.isEmailJaCadastrado()) {
            throw new EmailUsuarioJaEmUsoException("E-mail já está em uso.");
        }
    }
}
