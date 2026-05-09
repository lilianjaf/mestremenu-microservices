package com.github.lilianjaf.pagamento_service.infra.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lilianjaf.pagamento_service.core.dto.DadosProcessamentoPagamento;
import com.github.lilianjaf.pagamento_service.core.usecase.ProcessarPagamentoUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class PedidoCriadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoCriadoConsumer.class);

    private final ProcessarPagamentoUseCase processarPagamentoUseCase;
    private final ObjectMapper objectMapper;

    public PedidoCriadoConsumer(ProcessarPagamentoUseCase processarPagamentoUseCase,
                                ObjectMapper objectMapper) {
        this.processarPagamentoUseCase = processarPagamentoUseCase;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "pedido.criado", groupId = "pagamento-service-group")
    public void consumir(ConsumerRecord<String, String> record) {
        try {
            JsonNode payload = objectMapper.readTree(record.value());
            UUID pedidoId = UUID.fromString(payload.get("pedidoId").asText());
            BigDecimal valorTotal = new BigDecimal(payload.get("valorTotal").asText());

            log.info("Evento pedido.criado recebido — pedidoId: {}", pedidoId);
            processarPagamentoUseCase.executar(new DadosProcessamentoPagamento(pedidoId, valorTotal));
        } catch (Exception e) {
            log.error("Erro ao processar evento pedido.criado: {}", record.value(), e);
        }
    }
}
