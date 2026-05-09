package com.github.lilianjaf.pedido_service.infra.gateway;

import com.github.lilianjaf.pedido_service.infra.gateway.entity.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataOutboxRepository extends JpaRepository<OutboxEventEntity, UUID> {
    List<OutboxEventEntity> findByProcessadoFalse();
}
