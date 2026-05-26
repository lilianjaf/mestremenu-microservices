package com.github.lilianjaf.pagamento_service.core.usecase;

import com.github.lilianjaf.pagamento_service.core.domain.StatusPagamento;
import com.github.lilianjaf.pagamento_service.core.dto.DadosProcessamentoPagamento;
import com.github.lilianjaf.pagamento_service.core.gateway.ProcessadorPagamentoGateway;
import com.github.lilianjaf.pagamento_service.infra.gateway.SpringDataOutboxRepository;
import com.github.lilianjaf.pagamento_service.infra.gateway.SpringDataPagamentoRepository;
import com.github.lilianjaf.pagamento_service.infra.gateway.entity.PagamentoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import test.TestPagamentoServiceApplication;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TestPagamentoServiceApplication.class)
@ActiveProfiles("test")
@Transactional
class ProcessarPagamentoUseCaseIT {

    @Autowired private ProcessarPagamentoUseCase processarPagamentoUseCase;
    @Autowired private SpringDataPagamentoRepository pagamentoRepository;
    @Autowired private SpringDataOutboxRepository outboxRepository;

    @MockBean private ProcessadorPagamentoGateway processadorGateway;

    @Test
    @DisplayName("Deve salvar pagamento APROVADO e evento outbox quando processador aprova")
    void deveSalvarPagamentoAprovado() {
        UUID pedidoId = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        when(processadorGateway.processar(eq(pedidoId), eq(clienteId), any())).thenReturn(true);

        processarPagamentoUseCase.executar(new DadosProcessamentoPagamento(pedidoId, clienteId, BigDecimal.valueOf(100)));

        Optional<PagamentoEntity> saved = pagamentoRepository.findByPedidoId(pedidoId);
        assertTrue(saved.isPresent());
        assertEquals(StatusPagamento.APROVADO, saved.get().getStatus());
        assertEquals(0, saved.get().getTentativas());

        boolean eventoAprovado = outboxRepository.findByProcessadoFalse().stream()
                .anyMatch(e -> e.getAggregateId().equals(pedidoId) && e.getEventType().equals("pagamento.aprovado"));
        assertTrue(eventoAprovado);
    }

    @Test
    @DisplayName("Deve salvar pagamento PENDENTE quando processador rejeita na primeira tentativa")
    void deveSalvarPagamentoPendente() {
        UUID pedidoId = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        when(processadorGateway.processar(eq(pedidoId), eq(clienteId), any())).thenReturn(false);

        processarPagamentoUseCase.executar(new DadosProcessamentoPagamento(pedidoId, clienteId, BigDecimal.valueOf(50)));

        Optional<PagamentoEntity> saved = pagamentoRepository.findByPedidoId(pedidoId);
        assertTrue(saved.isPresent());
        assertEquals(StatusPagamento.PENDENTE, saved.get().getStatus());
        assertEquals(1, saved.get().getTentativas());

        boolean eventoPendente = outboxRepository.findByProcessadoFalse().stream()
                .anyMatch(e -> e.getAggregateId().equals(pedidoId) && e.getEventType().equals("pagamento.pendente"));
        assertTrue(eventoPendente);
    }

    @Test
    @DisplayName("Deve salvar pagamento FALHOU ao atingir max tentativas e atualizar registro existente")
    void deveSalvarPagamentoFalhouEAtualizarRegistroExistente() {
        UUID pedidoId = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        when(processadorGateway.processar(eq(pedidoId), eq(clienteId), any())).thenReturn(false);
        DadosProcessamentoPagamento dados = new DadosProcessamentoPagamento(pedidoId, clienteId, BigDecimal.valueOf(75));

        // First attempt → PENDENTE (tentativas=1, max=2)
        processarPagamentoUseCase.executar(dados);
        // Second attempt → FALHOU (tentativas=2 >= max=2) — exercises UPDATE path in PagamentoRepositoryJpaImpl
        processarPagamentoUseCase.executar(dados);

        Optional<PagamentoEntity> saved = pagamentoRepository.findByPedidoId(pedidoId);
        assertTrue(saved.isPresent());
        assertEquals(StatusPagamento.FALHOU, saved.get().getStatus());
        assertEquals(2, saved.get().getTentativas());

        boolean eventoFalhou = outboxRepository.findByProcessadoFalse().stream()
                .anyMatch(e -> e.getAggregateId().equals(pedidoId) && e.getEventType().equals("pagamento.falhou"));
        assertTrue(eventoFalhou);
    }

    @Test
    @DisplayName("Deve republificar evento quando pagamento já está APROVADO")
    void deveRepublicarEventoQuandoJaAprovado() {
        UUID pedidoId = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        when(processadorGateway.processar(eq(pedidoId), eq(clienteId), any())).thenReturn(true);
        DadosProcessamentoPagamento dados = new DadosProcessamentoPagamento(pedidoId, clienteId, BigDecimal.valueOf(200));

        processarPagamentoUseCase.executar(dados);
        long eventosAntes = outboxRepository.findByProcessadoFalse().stream()
                .filter(e -> e.getAggregateId().equals(pedidoId)).count();

        // Second call — already APROVADO → republish event without re-processing
        processarPagamentoUseCase.executar(dados);
        long eventosDepois = outboxRepository.findByProcessadoFalse().stream()
                .filter(e -> e.getAggregateId().equals(pedidoId)).count();

        assertEquals(eventosAntes + 1, eventosDepois);
    }

    @Test
    @DisplayName("Deve retornar lista de pagamentos por status")
    void deveBuscarPagamentosPorStatus() {
        UUID pedidoId = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        when(processadorGateway.processar(eq(pedidoId), eq(clienteId), any())).thenReturn(false);

        processarPagamentoUseCase.executar(new DadosProcessamentoPagamento(pedidoId, clienteId, BigDecimal.valueOf(30)));

        var pendentes = pagamentoRepository.findByStatus(StatusPagamento.PENDENTE);
        assertFalse(pendentes.isEmpty());
        assertTrue(pendentes.stream().anyMatch(p -> p.getPedidoId().equals(pedidoId)));
    }
}
