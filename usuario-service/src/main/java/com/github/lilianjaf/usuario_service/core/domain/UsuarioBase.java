package com.github.lilianjaf.usuario_service.core.domain;

import com.github.lilianjaf.usuario_service.core.exception.DomainException;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public abstract class UsuarioBase {

    private UUID id;
    private String nome;
    private String email;
    private String login;
    private String senha;
    private TipoUsuario tipoCustomizado;
    private Endereco endereco;
    private LocalDateTime dataUltimaAlteracao;
    private Boolean ativo;

    protected UsuarioBase(String nome, String email, String login, String senha, TipoUsuario tipoCustomizado, Endereco endereco) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.tipoCustomizado = tipoCustomizado;
        this.endereco = endereco;

        this.ativo = true;
        this.dataUltimaAlteracao = LocalDateTime.now();

        validarEstado();
    }

    protected UsuarioBase(UUID id, String nome, String email, String login, String senha, TipoUsuario tipoCustomizado, Endereco endereco, LocalDateTime dataUltimaAlteracao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.tipoCustomizado = tipoCustomizado;
        this.endereco = endereco;
        this.dataUltimaAlteracao = dataUltimaAlteracao;
        this.ativo = ativo;

        validarEstado();
    }

    public void desativar() {
        if (!this.ativo) {
            throw new DomainException("O usuário já está inativo.");
        }
        this.ativo = false;
        this.dataUltimaAlteracao = LocalDateTime.now();
    }

    public void atualizarEndereco(Endereco novoEndereco) {
        if (novoEndereco == null) {
            throw new DomainException("O novo endereço não pode ser nulo.");
        }
        this.endereco = novoEndereco;
        this.dataUltimaAlteracao = LocalDateTime.now();
    }

    public void atualizarDadosBasicos(String nome, String email) {
        if (nome == null || nome.isBlank()) throw new DomainException("Nome não pode ficar em branco.");
        if (email == null || email.isBlank()) throw new DomainException("Email não pode ficar em branco.");
        this.nome = nome;
        this.email = email;
        this.dataUltimaAlteracao = LocalDateTime.now();
    }

    private void validarEstado() {
        if (this.id == null) {
            throw new DomainException("Usuário deve ter um ID válido.");
        }
        if (this.nome == null || this.nome.isBlank()) {
            throw new DomainException("Nome é obrigatório.");
        }
        if (this.email == null || this.email.isBlank()) {
            throw new DomainException("Email é obrigatório.");
        }
        if (this.login == null || this.login.isBlank()) {
            throw new DomainException("Login é obrigatório.");
        }
        if (this.senha == null || this.senha.isBlank()) {
            throw new DomainException("Senha é obrigatória.");
        }
        if (this.tipoCustomizado == null) {
            throw new DomainException("O tipo de usuário é obrigatório.");
        }
        if (this.endereco == null) {
            throw new DomainException("O endereço é obrigatório para o usuário.");
        }
    }

    public boolean isDono() {
        return this instanceof Dono;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public TipoUsuario getTipoCustomizado() { return tipoCustomizado; }
    public Endereco getEndereco() { return endereco; }
    public LocalDateTime getDataUltimaAlteracao() { return dataUltimaAlteracao; }
    public Boolean getAtivo() { return ativo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioBase that = (UsuarioBase) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}