package com.github.lilianjaf.pagamento_service.infra.gateway.mapper;

import com.github.lilianjaf.pagamento_service.core.domain.Pagamento;
import com.github.lilianjaf.pagamento_service.infra.gateway.entity.PagamentoEntity;

public class PagamentoEntityMapper {

    public static Pagamento toDomain(PagamentoEntity entity) {
        return new Pagamento(
                entity.getId(),
                entity.getPedidoId(),
                entity.getValorTotal(),
                entity.getStatus(),
                entity.getTentativas(),
                entity.getCriadoEm(),
                entity.getProcessadoEm()
        );
    }

    public static PagamentoEntity toEntity(Pagamento domain) {
        return new PagamentoEntity(
                domain.getId(),
                domain.getPedidoId(),
                domain.getValorTotal(),
                domain.getStatus(),
                domain.getTentativas(),
                domain.getCriadoEm(),
                domain.getProcessadoEm()
        );
    }
}
