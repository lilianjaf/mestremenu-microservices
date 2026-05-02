package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.NomeTipoUsuarioJaEmUsoException;

public class NomeTipoUsuarioDeveSerUnicoRule implements ValidadorAtualizacaoTipoUsuarioRule {

    @Override
    public void validar(AtualizacaoTipoUsuarioContext context) {
        if (context.isNomeJaEmUso()) {
            throw new NomeTipoUsuarioJaEmUsoException("O nome '" + context.getNomeTipoComMesmoNome() + "' já está em uso por outro tipo de usuário.");
        }
    }
}
