package com.github.lilianjaf.usuario_service.core.domain;

import com.github.lilianjaf.usuario_service.core.exception.RegraDeNegocioException;

import java.util.UUID;

public class TipoUsuario {
    private UUID id;
    private String nome;
    private TipoNativo tipoNativo;

    public TipoUsuario(String nome, TipoNativo tipoNativo) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.tipoNativo = tipoNativo;
        validar();
    }

    public TipoUsuario(UUID id, String nome, TipoNativo tipoNativo) {
        this.id = id;
        this.nome = nome;
        this.tipoNativo = tipoNativo;
        validar();
    }

    public void atualizarNome(String novoNome) {
        this.nome = novoNome;
        validar();
    }

    private void validar() {
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException("O nome do tipo de usuário é obrigatório.");
        }
        if (tipoNativo == null) {
            throw new RegraDeNegocioException("O tipo de usuário nativo (DONO ou CLIENTE) é obrigatório para o tipo.");
        }
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public TipoNativo getTipoNativo() { return tipoNativo; }
}