package com.github.lilianjaf.pedido_service.infra.controller;

import java.util.List;
import java.util.UUID;

public record CriarPedidoJson(
        UUID restauranteId,
        List<ItemPedidoJson> itens
) {}
