package com.github.lilianjaf.pedido_service.core.dto;

import java.math.BigDecimal;

public record DadosCriacaoItemPedido(
        String descricao,
        int quantidade,
        BigDecimal preco
) {}
