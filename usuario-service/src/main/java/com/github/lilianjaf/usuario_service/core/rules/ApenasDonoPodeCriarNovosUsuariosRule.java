package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.AcessoNegadoCriacaoUsuarioException;

public class ApenasDonoPodeCriarNovosUsuariosRule implements ValidadorCriacaoUsuarioRule {

    @Override
    public void validar(CriacaoUsuarioContext context) {
        if (!context.isUsuarioLogadoDono()) {
            throw new AcessoNegadoCriacaoUsuarioException("Apenas usuários com o perfil de DONO podem criar outros usuários.");
        }
    }
}
