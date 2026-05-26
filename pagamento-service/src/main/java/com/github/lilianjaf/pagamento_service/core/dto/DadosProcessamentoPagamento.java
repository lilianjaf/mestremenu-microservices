package com.github.lilianjaf.pagamento_service.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record DadosProcessamentoPagamento(
        UUID pedidoId,
        UUID clienteId,
        BigDecimal valorTotal
) {}
