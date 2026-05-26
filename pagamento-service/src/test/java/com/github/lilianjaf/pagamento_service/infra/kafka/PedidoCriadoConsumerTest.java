package com.github.lilianjaf.pagamento_service.infra.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lilianjaf.pagamento_service.core.dto.DadosProcessamentoPagamento;
import com.github.lilianjaf.pagamento_service.core.usecase.ProcessarPagamentoUseCase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoCriadoConsumerTest {

    @Mock private ProcessarPagamentoUseCase processarPagamentoUseCase;
    @Spy private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks private PedidoCriadoConsumer consumer;

    private ConsumerRecord<String, String> record(String json) {
        return new ConsumerRecord<>("pedido.criado", 0, 0L, "key", json);
    }

    @Test
    @DisplayName("Deve chamar use case com dados corretos para JSON válido")
    void deveChamarUseCaseComJsonValido() {
        UUID pedidoId = UUID.randomUUID();
        String json = String.format(
                "{\"pedidoId\":\"%s\",\"valorTotal\":\"150.00\"}", pedidoId);

        consumer.consumir(record(json));

        ArgumentCaptor<DadosProcessamentoPagamento> captor =
                ArgumentCaptor.forClass(DadosProcessamentoPagamento.class);
        verify(processarPagamentoUseCase).executar(captor.capture());
        assertEquals(pedidoId, captor.getValue().pedidoId());
        assertEquals(new BigDecimal("150.00"), captor.getValue().valorTotal());
    }

    @Test
    @DisplayName("Deve silenciar erro e não propagar exceção para JSON inválido")
    void deveSilenciarErroParaJsonInvalido() {
        assertDoesNotThrow(() -> consumer.consumir(record("{invalid json}")));
        verify(processarPagamentoUseCase, never()).executar(any());
    }

    @Test
    @DisplayName("Deve silenciar erro quando use case lança exceção")
    void deveSilenciarErroQuandoUseCaseFalha() {
        UUID pedidoId = UUID.randomUUID();
        String json = String.format(
                "{\"pedidoId\":\"%s\",\"valorTotal\":\"50.00\"}", pedidoId);
        doThrow(new RuntimeException("DB error")).when(processarPagamentoUseCase).executar(any());

        assertDoesNotThrow(() -> consumer.consumir(record(json)));
    }
}
