package com.github.lilianjaf.pagamento_service.infra.gateway;

import com.github.lilianjaf.pagamento_service.core.domain.StatusPagamento;
import com.github.lilianjaf.pagamento_service.infra.gateway.entity.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataPagamentoRepository extends JpaRepository<PagamentoEntity, UUID> {
    Optional<PagamentoEntity> findByPedidoId(UUID pedidoId);
    List<PagamentoEntity> findByStatus(StatusPagamento status);
}
