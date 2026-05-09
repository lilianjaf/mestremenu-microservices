package com.github.lilianjaf.pedido_service.infra.gateway;

import com.github.lilianjaf.pedido_service.infra.gateway.entity.OutboxEventEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxRelayScheduler {

    private static final Logger log = LoggerFactory.getLogger(OutboxRelayScheduler.class);
    private static final String PEDIDO_CRIADO_TOPIC = "pedido.criado";

    private final SpringDataOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxRelayScheduler(SpringDataOutboxRepository outboxRepository,
                                KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    private String resolverTopico(String eventType) {
        return switch (eventType) {
            case "pedido.criado" -> PEDIDO_CRIADO_TOPIC;
            default -> {
                log.warn("Tipo de evento desconhecido: {}. Publicando em {}", eventType, PEDIDO_CRIADO_TOPIC);
                yield PEDIDO_CRIADO_TOPIC;
            }
        };
    }

    @Scheduled(fixedDelayString = "${outbox.relay.delay-ms:5000}")
    @Transactional
    public void processarEventosPendentes() {
        List<OutboxEventEntity> pendentes = outboxRepository.findByProcessadoFalse();
        for (OutboxEventEntity evento : pendentes) {
            try {
                String topico = resolverTopico(evento.getEventType());
                kafkaTemplate.send(topico, evento.getAggregateId().toString(), evento.getPayload());
                evento.setProcessado(true);
                outboxRepository.save(evento);
                log.info("Evento {} ({}) publicado no tópico {}", evento.getId(), evento.getEventType(), topico);
            } catch (Exception e) {
                log.error("Falha ao publicar evento {} — será reprocessado na próxima execução.", evento.getId(), e);
            }
        }
    }
}
