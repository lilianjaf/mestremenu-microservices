package com.github.lilianjaf.pedido_service.core.rules;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;

public record AtualizarStatusPedidoContext(
        Pedido pedido,
        StatusPedido novoStatus
) {}
