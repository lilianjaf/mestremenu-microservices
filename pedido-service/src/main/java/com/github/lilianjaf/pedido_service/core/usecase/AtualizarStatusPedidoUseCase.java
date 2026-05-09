package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;

import java.util.UUID;

public interface AtualizarStatusPedidoUseCase {
    Pedido executar(UUID pedidoId, StatusPedido novoStatus);
}
