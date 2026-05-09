package com.github.lilianjaf.pedido_service.core.usecase;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;

import java.util.UUID;

public interface ConfirmarPedidoUseCase {
    Pedido executar(UUID pedidoId);
}
