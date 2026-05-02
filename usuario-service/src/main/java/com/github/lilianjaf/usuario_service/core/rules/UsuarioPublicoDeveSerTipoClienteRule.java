package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.TipoUsuarioInvalidoParaCadastroPublicoException;

public class UsuarioPublicoDeveSerTipoClienteRule implements ValidadorCriacaoUsuarioPublicoRule {

    @Override
    public void validar(CriacaoUsuarioPublicoContext context) {
        if (!context.isTipoCliente()) {
            throw new TipoUsuarioInvalidoParaCadastroPublicoException("Apenas usuários do tipo CLIENTE podem ser criados via cadastro público.");
        }
    }
}
