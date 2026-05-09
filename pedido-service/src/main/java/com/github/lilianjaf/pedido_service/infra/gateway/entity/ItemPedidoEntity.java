package com.github.lilianjaf.pedido_service.infra.gateway.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "item_pedido")
public class ItemPedidoEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    protected ItemPedidoEntity() {}

    public ItemPedidoEntity(UUID id, PedidoEntity pedido, String descricao, int quantidade, BigDecimal preco) {
        this.id = id;
        this.pedido = pedido;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public UUID getId() { return id; }
    public PedidoEntity getPedido() { return pedido; }
    public String getDescricao() { return descricao; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPreco() { return preco; }
}
