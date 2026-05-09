package com.github.lilianjaf.pedido_service.core.dto;

import java.util.List;
import java.util.UUID;

public record DadosCriacaoPedido(
        UUID restauranteId,
        List<DadosCriacaoItemPedido> itens
) {}
