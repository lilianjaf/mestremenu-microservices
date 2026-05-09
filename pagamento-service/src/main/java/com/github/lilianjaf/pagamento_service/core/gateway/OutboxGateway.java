package com.github.lilianjaf.pagamento_service.core.gateway;

import com.github.lilianjaf.pagamento_service.core.domain.Pagamento;

public interface OutboxGateway {
    void salvarEventoPagamentoAprovado(Pagamento pagamento);
    void salvarEventoPagamentoPendente(Pagamento pagamento);
    void salvarEventoPagamentoFalhou(Pagamento pagamento);
}
