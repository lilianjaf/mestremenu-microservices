package com.github.lilianjaf.pedido_service.infra.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import com.github.lilianjaf.pedido_service.core.usecase.AtualizarStatusPedidoUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PagamentoEventoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PagamentoEventoConsumer.class);

    private final AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase;
    private final ObjectMapper objectMapper;

    public PagamentoEventoConsumer(AtualizarStatusPedidoUseCase atualizarStatusPedidoUseCase,
                                   ObjectMapper objectMapper) {
        this.atualizarStatusPedidoUseCase = atualizarStatusPedidoUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "pagamento.aprovado", groupId = "pedido-service-group")
    public void consumirPagamentoAprovado(ConsumerRecord<String, String> record) {
        processarEvento(record, StatusPedido.PAGO);
    }

    @KafkaListener(topics = "pagamento.pendente", groupId = "pedido-service-group")
    public void consumirPagamentoPendente(ConsumerRecord<String, String> record) {
        processarEvento(record, StatusPedido.PENDENTE_PAGAMENTO);
    }

    @KafkaListener(topics = "pagamento.falhou", groupId = "pedido-service-group")
    public void consumirPagamentoFalhou(ConsumerRecord<String, String> record) {
        processarEvento(record, StatusPedido.CANCELADO);
    }

    private void processarEvento(ConsumerRecord<String, String> record, StatusPedido novoStatus) {
        try {
            JsonNode payload = objectMapper.readTree(record.value());
            UUID pedidoId = UUID.fromString(payload.get("pedidoId").asText());
            atualizarStatusPedidoUseCase.executar(pedidoId, novoStatus);
            log.info("Status do pedido {} atualizado para {}", pedidoId, novoStatus);
        } catch (Exception e) {
            log.error("Erro ao processar evento de pagamento do tópico {}: {}",
                    record.topic(), record.value(), e);
        }
    }
}
