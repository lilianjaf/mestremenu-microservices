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

    public Usuario(UUID id, TipoNativo tipoNativo) {
        this.id = id;
        this.ativo = true;
        this.tipoCustomizado = new TipoUsuario(tipoNativo.name(), tipoNativo.name());
    }

    public UUID getId() {
        return id;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public boolean isDono() {
        return tipoCustomizado != null && tipoCustomizado.getTipoNativo().equals(TipoNativo.DONO);
    }

    public TipoNativo getTipoNativo() {
        return tipoCustomizado != null ? tipoCustomizado.getTipoNativo() : null;
    }
}
