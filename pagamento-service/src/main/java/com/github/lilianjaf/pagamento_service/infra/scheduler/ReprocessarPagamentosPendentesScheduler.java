package com.github.lilianjaf.pagamento_service.infra.scheduler;

import com.github.lilianjaf.pagamento_service.core.domain.Pagamento;
import com.github.lilianjaf.pagamento_service.core.domain.StatusPagamento;
import com.github.lilianjaf.pagamento_service.core.dto.DadosProcessamentoPagamento;
import com.github.lilianjaf.pagamento_service.core.gateway.PagamentoRepository;
import com.github.lilianjaf.pagamento_service.core.usecase.ProcessarPagamentoUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReprocessarPagamentosPendentesScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReprocessarPagamentosPendentesScheduler.class);

    private final PagamentoRepository pagamentoRepository;
    private final ProcessarPagamentoUseCase processarPagamentoUseCase;

    public ReprocessarPagamentosPendentesScheduler(PagamentoRepository pagamentoRepository,
                                                   ProcessarPagamentoUseCase processarPagamentoUseCase) {
        this.pagamentoRepository = pagamentoRepository;
        this.processarPagamentoUseCase = processarPagamentoUseCase;
    }

    @Scheduled(fixedDelayString = "${pagamento.reprocessar.delay-ms:30000}")
    public void reprocessar() {
        List<Pagamento> pendentes = pagamentoRepository.buscarPorStatus(StatusPagamento.PENDENTE);
        if (pendentes.isEmpty()) {
            return;
        }
        log.info("Reprocessando {} pagamento(s) pendente(s).", pendentes.size());
        for (Pagamento pagamento : pendentes) {
            try {
                processarPagamentoUseCase.executar(
                        new DadosProcessamentoPagamento(pagamento.getPedidoId(), pagamento.getClienteId(), pagamento.getValorTotal()));
            } catch (Exception e) {
                log.error("Erro ao reprocessar pagamento do pedido {}", pagamento.getPedidoId(), e);
            }
        }
    }
}
