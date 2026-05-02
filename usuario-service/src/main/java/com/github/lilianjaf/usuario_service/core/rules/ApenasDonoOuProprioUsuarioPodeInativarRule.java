package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.InativacaoUsuarioNaoAutorizadaException;

public class ApenasDonoOuProprioUsuarioPodeInativarRule implements ValidadorInativacaoUsuarioRule {

    @Override
    public void validar(InativacaoUsuarioContext context) {
        if (!context.isDonoOuProprioUsuarioAlvo()) {
            throw new InativacaoUsuarioNaoAutorizadaException("Apenas o próprio usuário ou um usuário do tipo DONO pode realizar esta inativação.");
        }
    }
}
