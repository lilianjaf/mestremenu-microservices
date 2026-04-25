package com.github.lilianjaf.restaurante_service.core.domain;

import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CardapioException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cardapio {

    private UUID id;
    private String nome;
    private UUID idRestaurante;
    private List<ItemCardapio> itens;

    public Cardapio(String nome, UUID idRestaurante, List<ItemCardapio> itens) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.idRestaurante = idRestaurante;
        this.itens = itens != null ? itens : new ArrayList<>();
        validarEstado();
    }

    public Cardapio(UUID id, String nome, UUID idRestaurante, List<ItemCardapio> itens) {
        this.id = id;
        this.nome = nome;
        this.idRestaurante = idRestaurante;
        this.itens = itens != null ? itens : new ArrayList<>();
        validarEstado();
    }

    private void validarEstado() {
        if (this.id == null) throw new CardapioException("Cardápio deve ter um ID válido.");
        if (this.nome == null || this.nome.isBlank()) throw new CardapioException("Nome é obrigatório para o cardápio.");
        if (this.idRestaurante == null) throw new CardapioException("O cardápio deve estar associado a um restaurante.");
    }

    public void atualizar(String nome, List<ItemCardapio> itens) {
        if (nome != null && !nome.isBlank()) this.nome = nome;
        if (itens != null) this.itens = itens;
        validarEstado();
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public UUID getIdRestaurante() { return idRestaurante; }
    public List<ItemCardapio> getItens() { return itens; }
}
