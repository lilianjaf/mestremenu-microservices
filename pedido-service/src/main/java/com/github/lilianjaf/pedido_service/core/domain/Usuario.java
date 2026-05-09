package com.github.lilianjaf.pedido_service.core.domain;

import java.util.UUID;

public class Usuario {
    private final UUID id;

    public Usuario(UUID id) {
        this.id = id;
    }

    public UUID getId() { return id; }
}
