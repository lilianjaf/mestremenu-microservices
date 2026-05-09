package com.github.lilianjaf.pedido_service.infra.gateway;

import com.github.lilianjaf.pedido_service.infra.gateway.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataPedidoRepository extends JpaRepository<PedidoEntity, UUID> {
    List<PedidoEntity> findByClienteId(UUID clienteId);
}
