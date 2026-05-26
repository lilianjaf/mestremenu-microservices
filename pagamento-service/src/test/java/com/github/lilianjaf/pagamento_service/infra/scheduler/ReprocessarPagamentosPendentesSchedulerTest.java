package com.github.lilianjaf.pagamento_service.infra.scheduler;

import com.github.lilianjaf.pagamento_service.core.domain.Pagamento;
import com.github.lilianjaf.pagamento_service.core.domain.StatusPagamento;
import com.github.lilianjaf.pagamento_service.core.dto.DadosProcessamentoPagamento;
import com.github.lilianjaf.pagamento_service.core.gateway.PagamentoRepository;
import com.github.lilianjaf.pagamento_service.core.usecase.ProcessarPagamentoUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReprocessarPagamentosPendentesSchedulerTest {

    @InjectMocks private ReprocessarPagamentosPendentesScheduler scheduler;
    @Mock private PagamentoRepository pagamentoRepository;
    @Mock private ProcessarPagamentoUseCase processarPagamentoUseCase;

    private Pagamento buildPagamento(UUID pedidoId, BigDecimal valor) {
        return new Pagamento(UUID.randomUUID(), pedidoId, valor, StatusPagamento.PENDENTE,
                1, LocalDateTime.now(), null);
    }

    @Test
    @DisplayName("Deve reprocessar pagamentos pendentes")
    void deveReprocessarPagamentosPendentes() {
        UUID pedidoId = UUID.randomUUID();
        BigDecimal valor = BigDecimal.valueOf(100);
        Pagamento pendente = buildPagamento(pedidoId, valor);
        when(pagamentoRepository.buscarPorStatus(StatusPagamento.PENDENTE)).thenReturn(List.of(pendente));

        scheduler.reprocessar();

        ArgumentCaptor<DadosProcessamentoPagamento> captor =
                ArgumentCaptor.forClass(DadosProcessamentoPagamento.class);
        verify(processarPagamentoUseCase).executar(captor.capture());
        assertEquals(pedidoId, captor.getValue().pedidoId());
        assertEquals(valor, captor.getValue().valorTotal());
    }

    @Test
    @DisplayName("Não deve fazer nada quando não há pagamentos pendentes")
    void naoDeveFazerNadaSemPendentes() {
        when(pagamentoRepository.buscarPorStatus(StatusPagamento.PENDENTE)).thenReturn(List.of());

        scheduler.reprocessar();

        verifyNoInteractions(processarPagamentoUseCase);
    }

    @Test
    @DisplayName("Deve continuar processando demais itens quando um falha")
    void deveContinuarAposFalha() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Pagamento p1 = buildPagamento(id1, BigDecimal.valueOf(10));
        Pagamento p2 = buildPagamento(id2, BigDecimal.valueOf(20));
        when(pagamentoRepository.buscarPorStatus(StatusPagamento.PENDENTE)).thenReturn(List.of(p1, p2));
        doThrow(new RuntimeException("erro")).when(processarPagamentoUseCase).executar(
                argThat(d -> d.pedidoId().equals(id1)));

        scheduler.reprocessar();

        verify(processarPagamentoUseCase, times(2)).executar(any());
    }
}
