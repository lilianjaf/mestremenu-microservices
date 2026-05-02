package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.UsuarioNaoEncontradoException;

public class UsuarioDeveExistirRule implements ValidadorAtualizacaoUsuarioRule {
    @Override
    public void validar(AtualizacaoUsuarioContext context) {
        if (!context.isUsuarioSendoEditadoExistente()) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }
    }
}
