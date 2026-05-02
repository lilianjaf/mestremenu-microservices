package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.UsuarioNaoEncontradoException;

public class ConsultaUsuarioDeveExistirRule implements ValidadorConsultaUsuarioRule {
    @Override
    public void validar(ConsultaUsuarioContext context) {
        if (!context.isUsuarioBuscadoExistente()) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado.");
        }
    }
}
