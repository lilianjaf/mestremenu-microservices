package com.github.lilianjaf.usuario_service.infra.gateway.entity;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tipo_usuario")
public class TipoUsuarioEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_nativo", nullable = false)
    private TipoNativo tipoNativo;

    protected TipoUsuarioEntity() {
    }

    public TipoUsuarioEntity(UUID id, String nome, TipoNativo tipoNativo) {
        this.id = id;
        this.nome = nome;
        this.tipoNativo = tipoNativo;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public TipoNativo getTipoNativo() { return tipoNativo; }
    public void setTipoNativo(TipoNativo tipoNativo) { this.tipoNativo = tipoNativo; }
}