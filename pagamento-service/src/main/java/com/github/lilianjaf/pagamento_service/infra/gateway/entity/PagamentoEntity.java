package com.github.lilianjaf.pagamento_service.infra.gateway.entity;

import com.github.lilianjaf.pagamento_service.core.domain.StatusPagamento;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pagamento")
public class PagamentoEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(name = "pedido_id", nullable = false, unique = true)
    private UUID pedidoId;

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "processado_em")
    private LocalDateTime processadoEm;

    @Transient
    private boolean isNew = true;

    protected PagamentoEntity() {}

    public PagamentoEntity(UUID id, UUID pedidoId, BigDecimal valorTotal,
                           StatusPagamento status, LocalDateTime criadoEm, LocalDateTime processadoEm) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.valorTotal = valorTotal;
        this.status = status;
        this.criadoEm = criadoEm;
        this.processadoEm = processadoEm;
    }

    @Override
    public UUID getId() { return id; }

    @Override
    public boolean isNew() { return isNew; }

    @PostLoad
    @PostPersist
    void markNotNew() { this.isNew = false; }

    public UUID getPedidoId() { return pedidoId; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getProcessadoEm() { return processadoEm; }
    public void setProcessadoEm(LocalDateTime processadoEm) { this.processadoEm = processadoEm; }
}
