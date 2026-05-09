package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.AcessoNegadoAoPedidoException;

public class ApenasProprioClientePodeConsultarPedidoRule implements ConsultarPedidoRule {

    @Override
    public void validar(ConsultarPedidoContext context) {
        if (!context.isPedidoDoCliente()) {
            throw new AcessoNegadoAoPedidoException("Você não tem permissão para visualizar este pedido.");
        }
    }
}
