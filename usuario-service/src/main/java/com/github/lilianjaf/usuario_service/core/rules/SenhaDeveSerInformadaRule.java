package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.mestremenuclean.usuario.core.exception.SenhaObrigatoriaNaoInformadaException;

public class SenhaDeveSerInformadaRule implements ValidadorCriacaoUsuarioRule {

    @Override
    public void validar(CriacaoUsuarioContext context) {
        if (!context.isSenhaInformada()) {
            throw new SenhaObrigatoriaNaoInformadaException("A senha deve ser informada para a criação do usuário.");
        }
    }
}
