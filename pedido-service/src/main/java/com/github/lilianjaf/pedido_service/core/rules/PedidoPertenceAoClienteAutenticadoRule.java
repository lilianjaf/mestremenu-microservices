package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.AcessoNegadoAoPedidoException;

public class PedidoPertenceAoClienteAutenticadoRule implements ConfirmarPedidoRule {

    @Override
    public void validar(ConfirmarPedidoContext context) {
        if (!context.isPedidoDoCliente()) {
            throw new AcessoNegadoAoPedidoException("Você não tem permissão para acessar este pedido.");
        }
    }
}
