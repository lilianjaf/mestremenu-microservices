package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.CriacaoTipoUsuarioNaoAutorizadaException;

public class ApenasDonoPodeCriarTipoUsuarioRule implements ValidadorCriacaoTipoUsuarioRule {
    @Override
    public void validar(CriacaoTipoUsuarioContext context) {
        if (!context.isUsuarioLogadoDono()) {
            throw new CriacaoTipoUsuarioNaoAutorizadaException("Apenas usuários com o perfil de 'DONO' possuem permissão para cadastrar novos tipos de usuário.");
        }
    }
}
