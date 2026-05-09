package com.github.lilianjaf.pedido_service.infra.gateway.entity;

import com.github.lilianjaf.pedido_service.core.domain.StatusPedido;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedido")
public class PedidoEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;

    @Column(name = "restaurante_id", nullable = false)
    private UUID restauranteId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedidoEntity> itens = new ArrayList<>();

    @Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Transient
    private boolean isNew = true;

    protected PedidoEntity() {}

    public PedidoEntity(UUID id, UUID clienteId, UUID restauranteId, BigDecimal valorTotal,
                        StatusPedido status, LocalDateTime dataCriacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.restauranteId = restauranteId;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    @Override
    public UUID getId() { return id; }

    @Override
    public boolean isNew() { return isNew; }

    @PostLoad
    @PostPersist
    void markNotNew() { this.isNew = false; }

    public UUID getClienteId() { return clienteId; }
    public UUID getRestauranteId() { return restauranteId; }
    public List<ItemPedidoEntity> getItens() { return itens; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
}
