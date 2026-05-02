package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.TipoUsuarioEmUsoException;

public class TipoUsuarioNaoDeveEstarEmUsoRule implements ValidadorExclusaoTipoUsuarioRule {
    @Override
    public void validar(ExclusaoTipoUsuarioContext context) {
        if (context.isTipoUsuarioEmUso()) {
            throw new TipoUsuarioEmUsoException("Não é possível excluir o tipo " + context.getNomeTipoUsuario() + " pois existem usuários vinculados a ele.");
        }
    }
}
