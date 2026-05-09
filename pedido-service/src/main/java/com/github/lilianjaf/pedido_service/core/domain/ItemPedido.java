package com.github.lilianjaf.pedido_service.core.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemPedido {

    private UUID id;
    private String descricao;
    private int quantidade;
    private BigDecimal preco;

    public ItemPedido(String descricao, int quantidade, BigDecimal preco) {
        this.id = UUID.randomUUID();
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public ItemPedido(UUID id, String descricao, int quantidade, BigDecimal preco) {
        this.id = id;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    public BigDecimal subtotal() {
        return preco.multiply(BigDecimal.valueOf(quantidade));
    }

    public UUID getId() { return id; }
    public String getDescricao() { return descricao; }
    public int getQuantidade() { return quantidade; }
    public BigDecimal getPreco() { return preco; }
}
