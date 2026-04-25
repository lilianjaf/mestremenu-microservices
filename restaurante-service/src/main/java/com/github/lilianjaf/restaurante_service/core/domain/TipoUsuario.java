package com.github.lilianjaf.restaurante_service.core.domain;

public class TipoUsuario {
    private String nome;
    private TipoNativo tipoNativo;

    public TipoUsuario(String nome, String tipo) {
        this.nome = nome;
        this.tipoNativo = TipoNativo.valueOf(tipo);
    }

    public String getNome() {
        return nome;
    }

    public TipoNativo getTipoNativo() {
        return tipoNativo;
    }
}