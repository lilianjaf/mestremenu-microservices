package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.UsuarioNaoAutenticadoException;

public class UsuarioDeveEstarAutenticadoParaConsultarPedidoRule implements ConsultarPedidoRule {

    @Override
    public void validar(ConsultarPedidoContext context) {
        if (!context.isUsuarioAutenticado()) {
            throw new UsuarioNaoAutenticadoException("É necessário estar autenticado para consultar pedidos.");
        }
    }
}
