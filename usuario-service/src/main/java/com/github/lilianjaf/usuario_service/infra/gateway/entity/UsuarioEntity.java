package com.github.lilianjaf.usuario_service.infra.gateway.entity;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class UsuarioEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "tipo_usuario_id", nullable = false)
    private TipoUsuarioEntity tipoCustomizado;

    @Embedded
    private EnderecoEmbeddable endereco;

    @Column(name = "data_ultima_alteracao", nullable = false)
    private LocalDateTime dataUltimaAlteracao;

    @Column(nullable = false)
    private Boolean ativo;

    @Transient
    private boolean isNew = true;

    protected UsuarioEntity() {
    }

    public UsuarioEntity(UUID id, String nome, String email, String login, String senha, TipoUsuarioEntity tipoCustomizado, EnderecoEmbeddable endereco, LocalDateTime dataUltimaAlteracao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.tipoCustomizado = tipoCustomizado;
        this.endereco = endereco;
        this.dataUltimaAlteracao = dataUltimaAlteracao;
        this.ativo = ativo;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public TipoUsuarioEntity getTipoCustomizado() { return tipoCustomizado; }
    public void setTipoCustomizado(TipoUsuarioEntity tipoCustomizado) { this.tipoCustomizado = tipoCustomizado; }
    public EnderecoEmbeddable getEndereco() { return endereco; }
    public void setEndereco(EnderecoEmbeddable endereco) { this.endereco = endereco; }
    public LocalDateTime getDataUltimaAlteracao() { return dataUltimaAlteracao; }
    public void setDataUltimaAlteracao(LocalDateTime dataUltimaAlteracao) { this.dataUltimaAlteracao = dataUltimaAlteracao; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }


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