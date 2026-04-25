package com.github.lilianjaf.restaurante_service.core.domain;

import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CardapioException;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemCardapio {

    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private boolean disponibilidadeRestaurante;
    private String caminhoFoto;
    private UUID idCardapio;

    public ItemCardapio(String nome, String descricao, BigDecimal preco, boolean disponibilidadeRestaurante, String caminhoFoto, UUID idCardapio) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponibilidadeRestaurante = disponibilidadeRestaurante;
        this.caminhoFoto = caminhoFoto;
        this.idCardapio = idCardapio;
        validarEstado();
    }

    public ItemCardapio(UUID id, String nome, String descricao, BigDecimal preco, boolean disponibilidadeRestaurante, String caminhoFoto, UUID idCardapio) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.disponibilidadeRestaurante = disponibilidadeRestaurante;
        this.caminhoFoto = caminhoFoto;
        this.idCardapio = idCardapio;
        validarEstado();
    }

    private void validarEstado() {
        if (this.id == null) throw new CardapioException("Item do cardápio deve ter um ID válido.");
        if (this.nome == null || this.nome.isBlank()) throw new CardapioException("Nome é obrigatório para o item do cardápio.");
        if (this.descricao == null || this.descricao.isBlank()) throw new CardapioException("Descrição é obrigatória para o item do cardápio.");
        if (this.preco == null) throw new CardapioException("Preço é obrigatório para o item do cardápio.");
        if (this.preco.compareTo(BigDecimal.ZERO) <= 0) throw new CardapioException("Preço do item do cardápio deve ser maior que zero.");
        if (this.caminhoFoto == null || this.caminhoFoto.isBlank()) throw new CardapioException("Caminho da foto é obrigatório para o item do cardápio.");
        if (this.idCardapio == null) throw new CardapioException("O item do cardápio deve estar associado a um cardápio.");
    }

    public void atualizar(String nome, String descricao, BigDecimal preco, boolean disponibilidadeRestaurante, String caminhoFoto) {
        if (nome != null && !nome.isBlank()) this.nome = nome;
        if (descricao != null && !descricao.isBlank()) this.descricao = descricao;
        if (preco != null) this.preco = preco;
        this.disponibilidadeRestaurante = disponibilidadeRestaurante;
        if (caminhoFoto != null && !caminhoFoto.isBlank()) this.caminhoFoto = caminhoFoto;
        validarEstado();
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public BigDecimal getPreco() { return preco; }
    public boolean isDisponibilidadeRestaurante() { return disponibilidadeRestaurante; }
    public String getCaminhoFoto() { return caminhoFoto; }
    public UUID getIdCardapio() { return idCardapio; }
}
