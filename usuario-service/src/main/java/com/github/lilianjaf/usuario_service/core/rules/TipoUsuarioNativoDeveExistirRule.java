package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.TipoUsuarioInvalidoException;

public class TipoUsuarioNativoDeveExistirRule implements ValidadorCriacaoUsuarioRule {

    @Override
    public void validar(CriacaoUsuarioContext context) {
        if (context.tipoNativo() == null ) {
            throw new TipoUsuarioInvalidoException("Tipo de usuário nativo inválido.");
        }
    }
}
