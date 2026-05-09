package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;

public class UsuarioDeveEstarAutenticadoParaConfirmarPedidoRule implements ConfirmarPedidoRule {

    @Override
    public void validar(ConfirmarPedidoContext context) {
        if (!context.isUsuarioAutenticado()) {
            throw new UsuarioNaoAutenticadoException("É necessário estar autenticado para confirmar um pedido.");
        }
    }
}
