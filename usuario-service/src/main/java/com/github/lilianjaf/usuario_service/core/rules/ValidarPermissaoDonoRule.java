package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.mestremenuclean.usuario.core.domain.UsuarioBase;
import com.github.lilianjaf.mestremenuclean.usuario.core.exception.AcessoNegadoException;

public class ValidarPermissaoDonoRule implements ValidadorPermissaoRule {

    @Override
    public void validar(UsuarioBase usuarioLogado) {
        if (!usuarioLogado.isDono()) {
            throw new AcessoNegadoException("Apenas um usuário 'dono' pode realizar esta operação.");
        }
    }
}
