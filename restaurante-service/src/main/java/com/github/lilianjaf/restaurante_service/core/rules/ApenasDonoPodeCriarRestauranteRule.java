package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.CriacaoRestauranteNaoAutorizadaException;

public class ApenasDonoPodeCriarRestauranteRule implements ValidadorCriacaoRestauranteRule {
    @Override
    public void validar(CriacaoRestauranteContext context) {
        if (!context.isUsuarioLogadoTipoDono()) {
            throw new CriacaoRestauranteNaoAutorizadaException("Apenas um usuário com perfil de DONO pode cadastrar um novo restaurante.");
        }
    }
}
