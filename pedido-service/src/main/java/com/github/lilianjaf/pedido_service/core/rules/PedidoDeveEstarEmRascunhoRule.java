package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.DomainException;

public class PedidoDeveEstarEmRascunhoRule implements ConfirmarPedidoRule {

    @Override
    public void validar(ConfirmarPedidoContext context) {
        if (!context.isPedidoEmRascunho()) {
            throw new DomainException("Apenas pedidos em rascunho podem ser confirmados.");
        }
    }
}
