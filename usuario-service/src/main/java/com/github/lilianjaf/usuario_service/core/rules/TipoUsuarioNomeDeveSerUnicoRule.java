package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.mestremenuclean.usuario.core.exception.TipoUsuarioJaCadastradoException;

public class TipoUsuarioNomeDeveSerUnicoRule implements ValidadorCriacaoTipoUsuarioRule {
    @Override
    public void validar(CriacaoTipoUsuarioContext context) {
        if (context.isNomeJaCadastrado()) {
            throw new TipoUsuarioJaCadastradoException("Já existe um tipo de usuário cadastrado com o nome: " + context.nome());
        }
    }
}
