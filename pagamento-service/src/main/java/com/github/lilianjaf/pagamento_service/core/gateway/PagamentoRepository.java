package com.github.lilianjaf.pagamento_service.core.gateway;

import com.github.lilianjaf.pagamento_service.core.domain.Pagamento;
import com.github.lilianjaf.pagamento_service.core.domain.StatusPagamento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PagamentoRepository {
    Pagamento salvar(Pagamento pagamento);
    Optional<Pagamento> buscarPorPedidoId(UUID pedidoId);
    List<Pagamento> buscarPorStatus(StatusPagamento status);
}
