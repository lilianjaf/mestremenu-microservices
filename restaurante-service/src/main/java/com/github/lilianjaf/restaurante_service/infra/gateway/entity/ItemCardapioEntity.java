package com.github.lilianjaf.restaurante_service.infra.gateway.entity;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "item_cardapio")
public class ItemCardapioEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(name = "disponibilidade_restaurante", nullable = false)
    private boolean disponibilidadeRestaurante;

    @Column(name = "caminho_foto", length = 500, nullable = false)
    private String caminhoFoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardapio_id", nullable = false)
    private CardapioEntity cardapio;

    @Transient
    private boolean isNew = true;

    protected ItemCardapioEntity() {}

    public ItemCardapioEntity(UUID id, String nome, String descricao, BigDecimal preco, boolean disponibilidadeRestaurante, String caminhoFoto, CardapioEntity cardapio) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponibilidadeRestaurante = disponibilidadeRestaurante;
        this.caminhoFoto = caminhoFoto;
        this.cardapio = cardapio;
    }

    @Override
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public boolean isDisponibilidadeRestaurante() { return disponibilidadeRestaurante; }
    public void setDisponibilidadeRestaurante(boolean disponibilidadeRestaurante) { this.disponibilidadeRestaurante = disponibilidadeRestaurante; }

    public String getCaminhoFoto() { return caminhoFoto; }
    public void setCaminhoFoto(String caminhoFoto) { this.caminhoFoto = caminhoFoto; }

    public CardapioEntity getCardapio() { return cardapio; }
    public void setCardapio(CardapioEntity cardapio) { this.cardapio = cardapio; }

    @Override
    public boolean isNew() { return isNew; }

    @PostLoad
    @PostPersist
    void markNotNew() { this.isNew = false; }
}
