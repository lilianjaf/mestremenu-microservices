package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.EmailUsuarioJaEmUsoException;

public class EmailUsuarioDeveSerUnicoRule implements ValidadorAtualizacaoUsuarioRule {
    @Override
    public void validar(AtualizacaoUsuarioContext context) {
        if (context.isConflitoDeEmail()) {
            throw new EmailUsuarioJaEmUsoException("E-mail já está em uso por outro usuário.");
        }
    }
}
