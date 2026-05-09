package com.github.lilianjaf.pedido_service.infra.controller;

import java.math.BigDecimal;

public record ItemPedidoJson(
        String descricao,
        int quantidade,
        BigDecimal preco
) {}
