package com.github.lilianjaf.pagamento_service.core.gateway;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProcessadorPagamentoGateway {
    boolean processar(UUID pedidoId, BigDecimal valorTotal);
}
