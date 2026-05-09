package com.github.lilianjaf.pedido_service.core.gateway;

import com.github.lilianjaf.pedido_service.core.domain.Pedido;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PedidoRepository {
    Pedido salvar(Pedido pedido);
    Optional<Pedido> buscarPorId(UUID id);
    List<Pedido> buscarPorClienteId(UUID clienteId);
}
