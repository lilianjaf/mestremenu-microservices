package com.github.lilianjaf.pedido_service.infra.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lilianjaf.pedido_service.core.domain.Pedido;
import com.github.lilianjaf.pedido_service.core.gateway.OutboxGateway;
import com.github.lilianjaf.pedido_service.infra.gateway.entity.OutboxEventEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class OutboxGatewayImpl implements OutboxGateway {

    private final SpringDataOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OutboxGatewayImpl(SpringDataOutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void salvarEventoPedidoCriado(Pedido pedido) {
        String payload = serializarPayload(pedido);
        OutboxEventEntity entity = new OutboxEventEntity(
                UUID.randomUUID(),
                pedido.getId(),
                "pedido.criado",
                payload,
                LocalDateTime.now(),
                false
        );
        outboxRepository.save(entity);
    }

    private String serializarPayload(Pedido pedido) {
        try {
            Map<String, Object> payload = Map.of(
                    "pedidoId", pedido.getId().toString(),
                    "clienteId", pedido.getClienteId().toString(),
                    "restauranteId", pedido.getRestauranteId().toString(),
                    "valorTotal", pedido.getValorTotal(),
                    "status", pedido.getStatus().name(),
                    "dataCriacao", pedido.getDataCriacao().toString()
            );
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar payload do evento de pedido.", e);
        }
    }
}
