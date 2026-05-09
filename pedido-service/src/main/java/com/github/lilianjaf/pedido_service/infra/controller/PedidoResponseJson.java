package com.github.lilianjaf.pedido_service.infra.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponseJson(
        UUID id,
        UUID clienteId,
        UUID restauranteId,
        List<ItemPedidoResponseJson> itens,
        BigDecimal valorTotal,
        String status,
        LocalDateTime dataCriacao
) {}
