package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.mestremenuclean.usuario.core.exception.TipoUsuarioNaoEncontradoException;

public class TipoUsuarioDeveExistirRule implements ValidadorExclusaoTipoUsuarioRule {
    @Override
    public void validar(ExclusaoTipoUsuarioContext context) {
        if (!context.isTipoUsuarioPresente()) {
            throw new TipoUsuarioNaoEncontradoException("Tipo de usuário não encontrado.");
        }
    }
}
