package com.github.lilianjaf.pagamento_service.core.usecase;

import com.github.lilianjaf.pagamento_service.core.domain.Pagamento;
import com.github.lilianjaf.pagamento_service.core.domain.StatusPagamento;
import com.github.lilianjaf.pagamento_service.core.dto.DadosProcessamentoPagamento;
import com.github.lilianjaf.pagamento_service.core.gateway.DomainLogger;
import com.github.lilianjaf.pagamento_service.core.gateway.OutboxGateway;
import com.github.lilianjaf.pagamento_service.core.gateway.PagamentoRepository;
import com.github.lilianjaf.pagamento_service.core.gateway.ProcessadorPagamentoGateway;
import com.github.lilianjaf.pagamento_service.core.gateway.TransactionGateway;

import java.util.Optional;

public class ProcessarPagamentoUseCaseImpl implements ProcessarPagamentoUseCase {

    private final PagamentoRepository pagamentoRepository;
    private final ProcessadorPagamentoGateway processadorGateway;
    private final OutboxGateway outboxGateway;
    private final TransactionGateway transactionGateway;
    private final DomainLogger log;
    private final int maxTentativas;

    public ProcessarPagamentoUseCaseImpl(PagamentoRepository pagamentoRepository,
                                         ProcessadorPagamentoGateway processadorGateway,
                                         OutboxGateway outboxGateway,
                                         TransactionGateway transactionGateway,
                                         DomainLogger log,
                                         int maxTentativas) {
        this.pagamentoRepository = pagamentoRepository;
        this.processadorGateway = processadorGateway;
        this.outboxGateway = outboxGateway;
        this.transactionGateway = transactionGateway;
        this.log = log;
        this.maxTentativas = maxTentativas;
    }

    @Override
    public void executar(DadosProcessamentoPagamento dados) {
        Optional<Pagamento> existente = pagamentoRepository.buscarPorPedidoId(dados.pedidoId());

        if (existente.isPresent()) {
            Pagamento p = existente.get();
            if (p.getStatus() == StatusPagamento.APROVADO) {
                log.info("Pedido {} já aprovado. Re-publicando evento.", dados.pedidoId());
                transactionGateway.execute(() -> outboxGateway.salvarEventoPagamentoAprovado(p));
                return;
            }
            if (p.getStatus() == StatusPagamento.FALHOU) {
                log.warn("Pedido {} já marcado como FALHOU. Ignorando reprocessamento.", dados.pedidoId());
                return;
            }
            log.info("Reprocessando pagamento existente para pedido {}", dados.pedidoId());
            tentarProcessar(p);
        } else {
            tentarProcessar(new Pagamento(dados.pedidoId(), dados.valorTotal()));
        }
    }

    private void tentarProcessar(Pagamento pagamento) {
        boolean aprovado = processadorGateway.processar(pagamento.getPedidoId(), pagamento.getValorTotal());

        // Gateway retornou false pode ser um timeout: verifica se pagamento já foi salvo como APROVADO
        if (!aprovado) {
            Optional<Pagamento> verificacao = pagamentoRepository.buscarPorPedidoId(pagamento.getPedidoId());
            if (verificacao.isPresent() && verificacao.get().getStatus() == StatusPagamento.APROVADO) {
                log.info("Pagamento do pedido {} encontrado como APROVADO no BD após timeout. Re-publicando evento.",
                        pagamento.getPedidoId());
                transactionGateway.execute(() -> outboxGateway.salvarEventoPagamentoAprovado(verificacao.get()));
                return;
            }
        }

        transactionGateway.execute(() -> {
            if (aprovado) {
                pagamento.aprovar();
                pagamentoRepository.salvar(pagamento);
                outboxGateway.salvarEventoPagamentoAprovado(pagamento);
                log.info("Pagamento do pedido {} APROVADO.", pagamento.getPedidoId());
            } else {
                pagamento.incrementarTentativa();
                if (pagamento.getTentativas() >= maxTentativas) {
                    pagamento.falhar();
                    pagamentoRepository.salvar(pagamento);
                    outboxGateway.salvarEventoPagamentoFalhou(pagamento);
                    log.warn("Pagamento do pedido {} FALHOU após {} tentativa(s). Cancelando pedido.",
                            pagamento.getPedidoId(), pagamento.getTentativas());
                } else {
                    pagamento.marcarPendente();
                    pagamentoRepository.salvar(pagamento);
                    outboxGateway.salvarEventoPagamentoPendente(pagamento);
                    log.warn("Pagamento do pedido {} PENDENTE (tentativa {}/{}).",
                            pagamento.getPedidoId(), pagamento.getTentativas(), maxTentativas);
                }
            }
        });
    }
}
