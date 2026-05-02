package com.github.lilianjaf.restaurante_service.core.domain;

import com.github.lilianjaf.restaurante_service.core.exception.DomainException;

import java.util.UUID;

public class Restaurante {

    private UUID id;
    private String nome;
    private Endereco endereco;
    private String tipoCozinha;
    private String horarioFuncionamento;
    private UUID idDono;
    private boolean ativo;

    public Restaurante(UUID id, UUID idDono) {
        this.id = id;
        this.idDono = idDono;
        this.nome = "Placeholder";
        this.endereco = null; // Careful here if used in validation
        this.tipoCozinha = "Placeholder";
        this.horarioFuncionamento = "Placeholder";
        this.ativo = true;
    }

    public Restaurante(String nome, Endereco endereco, String tipoCozinha, String horarioFuncionamento, UUID idDono) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
        this.horarioFuncionamento = horarioFuncionamento;
        this.idDono = idDono;
        this.ativo = true;
        validarEstado();
    }

    public Restaurante(UUID id, String nome, Endereco endereco, String tipoCozinha, String horarioFuncionamento, UUID idDono, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.tipoCozinha = tipoCozinha;
        this.horarioFuncionamento = horarioFuncionamento;
        this.idDono = idDono;
        this.ativo = ativo;
        validarEstado();
    }

    private void validarEstado() {
        if (this.id == null) throw new DomainException("Restaurante deve ter um ID válido.");
        if (this.nome == null || this.nome.isBlank()) throw new DomainException("Nome é obrigatório para o restaurante.");
        if (this.endereco == null) throw new DomainException("Endereço é obrigatório para o restaurante.");
        if (this.tipoCozinha == null || this.tipoCozinha.isBlank()) throw new DomainException("Tipo de cozinha é obrigatório.");
        if (this.horarioFuncionamento == null || this.horarioFuncionamento.isBlank()) throw new DomainException("Horário de funcionamento é obrigatório.");
        if (this.idDono == null) throw new DomainException("O restaurante deve estar associado a um dono.");
    }

    public void atualizar(String nome, Endereco endereco, String tipoCozinha, String horarioFuncionamento) {
        if (nome != null && !nome.isBlank()) this.nome = nome;
        if (endereco != null) this.endereco = endereco;
        if (tipoCozinha != null && !tipoCozinha.isBlank()) this.tipoCozinha = tipoCozinha;
        if (horarioFuncionamento != null && !horarioFuncionamento.isBlank()) this.horarioFuncionamento = horarioFuncionamento;
        validarEstado();
    }

    public void inativar() {
        this.ativo = false;
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public Endereco getEndereco() { return endereco; }
    public String getTipoCozinha() { return tipoCozinha; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public UUID getIdDono() { return idDono; }
    public boolean isAtivo() { return ativo; }
}
