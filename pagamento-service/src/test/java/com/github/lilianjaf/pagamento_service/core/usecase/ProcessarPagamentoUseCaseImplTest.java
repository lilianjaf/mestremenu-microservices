package com.github.lilianjaf.pagamento_service.core.usecase;

import com.github.lilianjaf.pagamento_service.core.domain.Pagamento;
import com.github.lilianjaf.pagamento_service.core.domain.StatusPagamento;
import com.github.lilianjaf.pagamento_service.core.dto.DadosProcessamentoPagamento;
import com.github.lilianjaf.pagamento_service.core.gateway.DomainLogger;
import com.github.lilianjaf.pagamento_service.core.gateway.OutboxGateway;
import com.github.lilianjaf.pagamento_service.core.gateway.PagamentoRepository;
import com.github.lilianjaf.pagamento_service.core.gateway.ProcessadorPagamentoGateway;
import com.github.lilianjaf.pagamento_service.core.gateway.TransactionGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessarPagamentoUseCaseImplTest {

    @Mock private PagamentoRepository pagamentoRepository;
    @Mock private ProcessadorPagamentoGateway processadorGateway;
    @Mock private OutboxGateway outboxGateway;
    @Mock private TransactionGateway transactionGateway;
    @Mock private DomainLogger log;

    private ProcessarPagamentoUseCaseImpl useCase;

    private static final int MAX_TENTATIVAS = 3;
    private static final BigDecimal VALOR = new BigDecimal("100.00");

    private final UUID pedidoId = UUID.randomUUID();
    private final DadosProcessamentoPagamento dados = new DadosProcessamentoPagamento(pedidoId, VALOR);

    @BeforeEach
    void setUp() {
        lenient().doAnswer(invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        }).when(transactionGateway).execute(any(Runnable.class));

        useCase = new ProcessarPagamentoUseCaseImpl(
                pagamentoRepository, processadorGateway, outboxGateway,
                transactionGateway, log, MAX_TENTATIVAS);
    }

    private Pagamento pagamentoComStatus(StatusPagamento status, int tentativas) {
        return new Pagamento(UUID.randomUUID(), pedidoId, VALOR, status,
                tentativas, LocalDateTime.now(), null);
    }

    @Test
    @DisplayName("Deve aprovar pagamento novo quando processador retorna sucesso")
    void deveAprovarPagamentoNovo() {
        when(pagamentoRepository.buscarPorPedidoId(pedidoId)).thenReturn(Optional.empty());
        when(processadorGateway.processar(eq(pedidoId), eq(VALOR))).thenReturn(true);

        ArgumentCaptor<Pagamento> captor = ArgumentCaptor.forClass(Pagamento.class);
        when(pagamentoRepository.salvar(captor.capture())).thenAnswer(i -> i.getArgument(0));

        useCase.executar(dados);

        assertEquals(StatusPagamento.APROVADO, captor.getValue().getStatus());
        verify(outboxGateway).salvarEventoPagamentoAprovado(any());
        verify(outboxGateway, never()).salvarEventoPagamentoPendente(any());
        verify(outboxGateway, never()).salvarEventoPagamentoFalhou(any());
    }

    @Test
    @DisplayName("Deve marcar como PENDENTE na primeira rejeição (tentativa 1 de 3)")
    void deveMarcarPendenteNaPrimeiraRejeicao() {
        when(pagamentoRepository.buscarPorPedidoId(pedidoId)).thenReturn(Optional.empty());
        when(processadorGateway.processar(eq(pedidoId), eq(VALOR))).thenReturn(false);

        ArgumentCaptor<Pagamento> captor = ArgumentCaptor.forClass(Pagamento.class);
        when(pagamentoRepository.salvar(captor.capture())).thenAnswer(i -> i.getArgument(0));

        useCase.executar(dados);

        Pagamento salvo = captor.getValue();
        assertEquals(StatusPagamento.PENDENTE, salvo.getStatus());
        assertEquals(1, salvo.getTentativas());
        verify(outboxGateway).salvarEventoPagamentoPendente(any());
        verify(outboxGateway, never()).salvarEventoPagamentoFalhou(any());
    }

    @Test
    @DisplayName("Deve marcar como FALHOU ao atingir o número máximo de tentativas")
    void deveMarcarFalhouAoAtingirMaxTentativas() {
        Pagamento pendente = pagamentoComStatus(StatusPagamento.PENDENTE, MAX_TENTATIVAS - 1);
        when(pagamentoRepository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.of(pendente))
                .thenReturn(Optional.empty());
        when(processadorGateway.processar(eq(pedidoId), eq(VALOR))).thenReturn(false);

        ArgumentCaptor<Pagamento> captor = ArgumentCaptor.forClass(Pagamento.class);
        when(pagamentoRepository.salvar(captor.capture())).thenAnswer(i -> i.getArgument(0));

        useCase.executar(dados);

        Pagamento salvo = captor.getValue();
        assertEquals(StatusPagamento.FALHOU, salvo.getStatus());
        assertEquals(MAX_TENTATIVAS, salvo.getTentativas());
        verify(outboxGateway).salvarEventoPagamentoFalhou(any());
        verify(outboxGateway, never()).salvarEventoPagamentoPendente(any());
    }

    @Test
    @DisplayName("Deve re-publicar evento aprovado quando pagamento já está APROVADO")
    void deveRePublicarEventoQuandoPagamentoJaAprovado() {
        Pagamento aprovado = pagamentoComStatus(StatusPagamento.APROVADO, 1);
        when(pagamentoRepository.buscarPorPedidoId(pedidoId)).thenReturn(Optional.of(aprovado));

        useCase.executar(dados);

        verify(processadorGateway, never()).processar(any(), any());
        verify(outboxGateway).salvarEventoPagamentoAprovado(aprovado);
        verify(pagamentoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve ignorar reprocessamento quando pagamento já está FALHOU")
    void deveIgnorarQuandoPagamentoJaFalhou() {
        Pagamento falhou = pagamentoComStatus(StatusPagamento.FALHOU, MAX_TENTATIVAS);
        when(pagamentoRepository.buscarPorPedidoId(pedidoId)).thenReturn(Optional.of(falhou));

        useCase.executar(dados);

        verify(processadorGateway, never()).processar(any(), any());
        verify(outboxGateway, never()).salvarEventoPagamentoFalhou(any());
        verify(pagamentoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve re-publicar evento aprovado no caso de timeout (DB já mostra APROVADO)")
    void deveRePublicarEventoNoTimeoutEdgeCase() {
        Pagamento aprovadoNoDB = pagamentoComStatus(StatusPagamento.APROVADO, 0);
        when(pagamentoRepository.buscarPorPedidoId(pedidoId))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(aprovadoNoDB));
        when(processadorGateway.processar(eq(pedidoId), eq(VALOR))).thenReturn(false);

        useCase.executar(dados);

        verify(outboxGateway).salvarEventoPagamentoAprovado(aprovadoNoDB);
        verify(outboxGateway, never()).salvarEventoPagamentoPendente(any());
        verify(pagamentoRepository, never()).salvar(any());
    }

    @Test
    @DisplayName("Deve aprovar pagamento pendente que é reprocessado com sucesso")
    void deveAprovarPagamentoPendenteReprocessadoComSucesso() {
        Pagamento pendente = pagamentoComStatus(StatusPagamento.PENDENTE, 1);
        when(pagamentoRepository.buscarPorPedidoId(pedidoId)).thenReturn(Optional.of(pendente));
        when(processadorGateway.processar(eq(pedidoId), eq(VALOR))).thenReturn(true);

        ArgumentCaptor<Pagamento> captor = ArgumentCaptor.forClass(Pagamento.class);
        when(pagamentoRepository.salvar(captor.capture())).thenAnswer(i -> i.getArgument(0));

        useCase.executar(dados);

        assertEquals(StatusPagamento.APROVADO, captor.getValue().getStatus());
        verify(outboxGateway).salvarEventoPagamentoAprovado(any());
    }
}
