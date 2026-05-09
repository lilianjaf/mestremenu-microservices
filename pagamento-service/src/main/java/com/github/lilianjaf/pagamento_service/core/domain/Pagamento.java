package com.github.lilianjaf.pagamento_service.core.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pagamento {

    private UUID id;
    private UUID pedidoId;
    private BigDecimal valorTotal;
    private StatusPagamento status;
    private int tentativas;
    private LocalDateTime criadoEm;
    private LocalDateTime processadoEm;

    public Pagamento(UUID pedidoId, BigDecimal valorTotal) {
        this.id = UUID.randomUUID();
        this.pedidoId = pedidoId;
        this.valorTotal = valorTotal;
        this.status = StatusPagamento.AGUARDANDO;
        this.tentativas = 0;
        this.criadoEm = LocalDateTime.now();
    }

    public Pagamento(UUID id, UUID pedidoId, BigDecimal valorTotal, StatusPagamento status,
                     int tentativas, LocalDateTime criadoEm, LocalDateTime processadoEm) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.valorTotal = valorTotal;
        this.status = status;
        this.tentativas = tentativas;
        this.criadoEm = criadoEm;
        this.processadoEm = processadoEm;
    }

    public void aprovar() {
        this.status = StatusPagamento.APROVADO;
        this.processadoEm = LocalDateTime.now();
    }

    public void marcarPendente() {
        this.status = StatusPagamento.PENDENTE;
    }

    public void incrementarTentativa() {
        this.tentativas++;
    }

    public void falhar() {
        this.status = StatusPagamento.FALHOU;
        this.processadoEm = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getPedidoId() { return pedidoId; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public StatusPagamento getStatus() { return status; }
    public int getTentativas() { return tentativas; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getProcessadoEm() { return processadoEm; }
}
