package com.github.lilianjaf.pedido_service.core.gateway;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;

public interface OutboxGateway {
    void salvarEventoPedidoCriado(Pedido pedido);
}
