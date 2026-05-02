package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.AcessoNegadoExclusaoTipoUsuarioException;

public class ApenasDonoPodeDeletarTipoUsuarioRule implements ValidadorExclusaoTipoUsuarioRule {
    @Override
    public void validar(ExclusaoTipoUsuarioContext context) {
        if (!context.isUsuarioLogadoDono()) {
            throw new AcessoNegadoExclusaoTipoUsuarioException("Apenas usuários do tipo DONO podem excluir tipos de usuário.");
        }
    }
}
