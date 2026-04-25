package com.github.lilianjaf.restaurante_service.infra.gateway.entity;

import com.github.lilianjaf.mestremenuclean.restaurante.infra.gateway.entity.EnderecoEmbeddable;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.util.UUID;

@Entity
@Table(name = "restaurante")
public class RestauranteEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Embedded
    private EnderecoEmbeddable endereco;

    @Column(name = "tipo_cozinha", nullable = false)
    private String tipoCozinha;

    @Column(name = "horario_funcionamento", nullable = false)
    private String horarioFuncionamento;

    @Column(name = "id_dono", nullable = false)
    private UUID idDono;

    @Column(nullable = false)
    private boolean ativo = true;

    @Transient
    private boolean isNew = true;

    protected RestauranteEntity() {}

    public RestauranteEntity(UUID id, String nome, EnderecoEmbeddable endereco, String tipoCozinha, String horarioFuncionamento, UUID idDono, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
        this.horarioFuncionamento = horarioFuncionamento;
        this.idDono = idDono;
        this.ativo = ativo;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public EnderecoEmbeddable getEndereco() { return endereco; }
    public void setEndereco(EnderecoEmbeddable endereco) { this.endereco = endereco; }
    
    public String getTipoCozinha() { return tipoCozinha; }
    public void setTipoCozinha(String tipoCozinha) { this.tipoCozinha = tipoCozinha; }
    
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) { this.horarioFuncionamento = horarioFuncionamento; }
    
    public UUID getIdDono() { return idDono; }
    public void setIdDono(UUID idDono) { this.idDono = idDono; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }
}
