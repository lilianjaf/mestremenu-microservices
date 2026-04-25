package com.github.lilianjaf.restaurante_service.core.domain;

import java.util.UUID;

public class Usuario {
    private UUID id;
    private boolean ativo;
    private TipoUsuario tipoCustomizado;

    public Usuario(UUID id, boolean ativo, TipoUsuario tipoCustomizado) {
        this.id = id;
        this.ativo = ativo;
        this.tipoCustomizado = tipoCustomizado;
    }

    public UUID getId() {
        return id;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public boolean isDono() {
        return tipoCustomizado.getTipoNativo().equals(TipoNativo.DONO);
    }
}
