package com.github.lilianjaf.pagamento_service.core.usecase;

import com.github.lilianjaf.pagamento_service.core.dto.DadosProcessamentoPagamento;

public interface ProcessarPagamentoUseCase {
    void executar(DadosProcessamentoPagamento dados);
}
