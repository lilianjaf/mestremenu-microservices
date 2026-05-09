package com.github.lilianjaf.pagamento_service.infra.gateway;

import com.github.lilianjaf.pagamento_service.infra.gateway.entity.OutboxEventEntity;
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

    private final SpringDataOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxRelayScheduler(SpringDataOutboxRepository outboxRepository,
                                KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${outbox.relay.delay-ms:5000}")
    @Transactional
    public void processarEventosPendentes() {
        List<OutboxEventEntity> pendentes = outboxRepository.findByProcessadoFalse();
        for (OutboxEventEntity evento : pendentes) {
            try {
                kafkaTemplate.send(evento.getEventType(), evento.getAggregateId().toString(), evento.getPayload());
                evento.setProcessado(true);
                outboxRepository.save(evento);
                log.info("Evento {} ({}) publicado.", evento.getId(), evento.getEventType());
            } catch (Exception e) {
                log.error("Falha ao publicar evento {} — será reprocessado.", evento.getId(), e);
            }
        }
    }
}
