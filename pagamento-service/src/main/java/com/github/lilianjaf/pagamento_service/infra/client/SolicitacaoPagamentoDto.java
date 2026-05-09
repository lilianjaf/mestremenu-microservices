package com.github.lilianjaf.pagamento_service.infra.client;

import java.math.BigDecimal;

public record SolicitacaoPagamentoDto(
        String pedidoId,
        BigDecimal valor
) {}
