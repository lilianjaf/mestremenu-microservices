package com.github.lilianjaf.pagamento_service.infra.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lilianjaf.pagamento_service.core.domain.Pagamento;
import com.github.lilianjaf.pagamento_service.core.gateway.OutboxGateway;
import com.github.lilianjaf.pagamento_service.infra.gateway.entity.OutboxEventEntity;
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
    public void salvarEventoPagamentoAprovado(Pagamento pagamento) {
        salvar(pagamento, "pagamento.aprovado");
    }

    @Override
    public void salvarEventoPagamentoPendente(Pagamento pagamento) {
        salvar(pagamento, "pagamento.pendente");
    }

    @Override
    public void salvarEventoPagamentoFalhou(Pagamento pagamento) {
        salvar(pagamento, "pagamento.falhou");
    }

    private void salvar(Pagamento pagamento, String eventType) {
        String payload = serializarPayload(pagamento);
        OutboxEventEntity entity = new OutboxEventEntity(
                UUID.randomUUID(),
                pagamento.getPedidoId(),
                eventType,
                payload,
                LocalDateTime.now(),
                false
        );
        outboxRepository.save(entity);
    }

    private String serializarPayload(Pagamento pagamento) {
        try {
            Map<String, Object> payload = Map.of(
                    "pedidoId", pagamento.getPedidoId().toString(),
                    "pagamentoId", pagamento.getId().toString(),
                    "valorTotal", pagamento.getValorTotal(),
                    "status", pagamento.getStatus().name()
            );
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar payload do evento de pagamento.", e);
        }
    }
}
