package com.github.lilianjaf.pedido_service.infra.controller;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoResponseJson(
        UUID id,
        String descricao,
        int quantidade,
        BigDecimal preco,
        BigDecimal subtotal
) {}
