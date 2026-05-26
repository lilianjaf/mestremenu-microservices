package com.github.lilianjaf.pagamento_service.infra.gateway;

import com.github.lilianjaf.pagamento_service.infra.client.ProcessadorPagamentoClient;
import com.github.lilianjaf.pagamento_service.infra.client.RespostaPagamentoDto;
import com.github.lilianjaf.pagamento_service.infra.client.SolicitacaoPagamentoDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessadorPagamentoGatewayImplTest {

    @InjectMocks private ProcessadorPagamentoGatewayImpl gateway;
    @Mock private ProcessadorPagamentoClient client;

    @Test
    @DisplayName("processar deve retornar true quando cliente responde com sucesso")
    void deveRetornarTrueQuandoClienteResponde() {
        when(client.processar(any(SolicitacaoPagamentoDto.class)))
                .thenReturn(new RespostaPagamentoDto("APROVADO", "ok"));

        boolean resultado = gateway.processar(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(100));

        assertTrue(resultado);
        verify(client).processar(any(SolicitacaoPagamentoDto.class));
    }

    @Test
    @DisplayName("processar deve retornar false via fallback quando cliente lança exceção")
    void deveRetornarFalseViaFallback() {
        when(client.processar(any(SolicitacaoPagamentoDto.class)))
                .thenThrow(new RuntimeException("connection refused"));

        boolean resultado = gateway.processar(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(50));

        assertFalse(resultado);
    }

    @Test
    @DisplayName("fallbackAsync deve retornar CompletableFuture com false")
    void fallbackAsyncDeveRetornarFalse() throws Exception {
        UUID pedidoId = UUID.randomUUID();
        CompletableFuture<Boolean> resultado = gateway.fallbackAsync(
                pedidoId, UUID.randomUUID(), BigDecimal.valueOf(10), new RuntimeException("timeout"));

        assertFalse(resultado.get());
    }

    @Test
    @DisplayName("SolicitacaoPagamentoDto deve armazenar pedidoId e valor")
    void solicitacaoDtoDeveArmazenarDados() {
        SolicitacaoPagamentoDto dto = new SolicitacaoPagamentoDto("id-123", "client-456", 99L);
        assertEquals("id-123", dto.pagamentoId());
        assertEquals("client-456", dto.clienteId());
        assertEquals(99L, dto.valor());
    }

    @Test
    @DisplayName("RespostaPagamentoDto deve armazenar status e mensagem")
    void respostaDtoDeveArmazenarDados() {
        RespostaPagamentoDto dto = new RespostaPagamentoDto("APROVADO", "sucesso");
        assertEquals("APROVADO", dto.status());
        assertEquals("sucesso", dto.mensagem());
    }
}
