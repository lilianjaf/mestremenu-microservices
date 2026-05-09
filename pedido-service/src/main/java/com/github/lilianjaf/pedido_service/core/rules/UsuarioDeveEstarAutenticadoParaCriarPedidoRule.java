package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;

public class UsuarioDeveEstarAutenticadoParaCriarPedidoRule implements CriarPedidoRule {

    @Override
    public void validar(CriarPedidoContext context) {
        if (!context.isUsuarioAutenticado()) {
            throw new UsuarioNaoAutenticadoException("É necessário estar autenticado para criar um pedido.");
        }
    }
}
