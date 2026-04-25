package com.github.lilianjaf.restaurante_service.infra.gateway.entity;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cardapio")
public class CardapioEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "id_restaurante", nullable = false)
    private UUID idRestaurante;

    @OneToMany(mappedBy = "cardapio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemCardapioEntity> itens = new ArrayList<>();

    @Transient
    private boolean isNew = true;

    protected CardapioEntity() {}

    public CardapioEntity(UUID id, String nome, UUID idRestaurante) {
        this.id = id;
        this.nome = nome;
        this.idRestaurante = idRestaurante;
    }

    @Override
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public UUID getIdRestaurante() { return idRestaurante; }
    public void setIdRestaurante(UUID idRestaurante) { this.idRestaurante = idRestaurante; }

    public List<ItemCardapioEntity> getItens() { return itens; }
    public void setItens(List<ItemCardapioEntity> itens) {
        this.itens = itens;
        if (itens != null) {
            itens.forEach(item -> item.setCardapio(this));
        }
    }

    @Override
    public boolean isNew() { return isNew; }

    @PostLoad
    @PostPersist
    void markNotNew() { this.isNew = false; }
}
