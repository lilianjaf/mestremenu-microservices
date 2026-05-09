package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.exception.DomainException;

public class PedidoDeveConterItensRule implements CriarPedidoRule {

    @Override
    public void validar(CriarPedidoContext context) {
        if (context.isItensPedidoVazio()) {
            throw new DomainException("O pedido deve conter pelo menos um item.");
        }
    }
}
