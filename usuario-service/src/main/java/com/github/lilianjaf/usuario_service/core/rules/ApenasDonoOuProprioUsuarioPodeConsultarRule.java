package com.github.lilianjaf.usuario_service.core.rules;

import com.github.lilianjaf.usuario_service.core.exception.AcessoNegadoConsultaUsuarioException;

public class ApenasDonoOuProprioUsuarioPodeConsultarRule implements ValidadorConsultaUsuarioRule {
    @Override
    public void validar(ConsultaUsuarioContext context) {
        if (context.usuarioLogado() == null) {
            return;
        }

        if (!context.isDonoOuProprioUsuario()) {
            throw new AcessoNegadoConsultaUsuarioException("Você não tem permissão para consultar este usuário");
        }
    }
}
