package com.github.lilianjaf.pagamento_service.infra.gateway;

import com.github.lilianjaf.pagamento_service.infra.gateway.entity.OutboxEventEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxRelaySchedulerTest {

    @InjectMocks private OutboxRelayScheduler scheduler;
    @Mock private SpringDataOutboxRepository outboxRepository;
    @Mock private KafkaTemplate<String, String> kafkaTemplate;

    private OutboxEventEntity buildEvento(String eventType) {
        return new OutboxEventEntity(
                UUID.randomUUID(), UUID.randomUUID(), eventType,
                "{\"key\":\"value\"}", LocalDateTime.now(), false);
    }

    @Test
    @DisplayName("Deve publicar evento e marcar como processado")
    void devePublicarEventoComSucesso() {
        OutboxEventEntity evento = buildEvento("pagamento.aprovado");
        when(outboxRepository.findByProcessadoFalse()).thenReturn(List.of(evento));

        scheduler.processarEventosPendentes();

        verify(kafkaTemplate).send(eq("pagamento.aprovado"), anyString(), anyString());
        verify(outboxRepository).save(evento);
        assertTrue(evento.isProcessado());
    }

    @Test
    @DisplayName("Não deve marcar como processado quando Kafka falha")
    void deveManterNaoProcessadoQuandoKafkaFalha() {
        OutboxEventEntity evento = buildEvento("pagamento.aprovado");
        when(outboxRepository.findByProcessadoFalse()).thenReturn(List.of(evento));
        doThrow(new RuntimeException("Kafka unavailable"))
                .when(kafkaTemplate).send(anyString(), anyString(), anyString());

        scheduler.processarEventosPendentes();

        assertFalse(evento.isProcessado());
        verify(outboxRepository, never()).save(evento);
    }

    @Test
    @DisplayName("Não deve fazer nada quando não há eventos pendentes")
    void naoDeveFazerNadaSemEventosPendentes() {
        when(outboxRepository.findByProcessadoFalse()).thenReturn(List.of());

        scheduler.processarEventosPendentes();

        verifyNoInteractions(kafkaTemplate);
    }

    @Test
    @DisplayName("Deve processar múltiplos eventos em sequência")
    void deveProcessarMultiplosEventos() {
        OutboxEventEntity e1 = buildEvento("pagamento.aprovado");
        OutboxEventEntity e2 = buildEvento("pagamento.falhou");
        when(outboxRepository.findByProcessadoFalse()).thenReturn(List.of(e1, e2));

        scheduler.processarEventosPendentes();

        verify(kafkaTemplate, times(2)).send(anyString(), anyString(), anyString());
        assertTrue(e1.isProcessado());
        assertTrue(e2.isProcessado());
    }
}
