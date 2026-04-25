package com.github.lilianjaf.restaurante_service.core.domain;

import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.DomainException;

public record Endereco (
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String cep,
        String uf
) {
    public Endereco {
        if (logradouro == null || logradouro.isBlank()) {
            throw new DomainException("Logradouro é obrigatório para um endereço válido.");
        }
        if (numero == null || numero.isBlank()) {
            throw new DomainException("Número é obrigatório para um endereço válido.");
        }
        if (bairro == null || bairro.isBlank()) {
            throw new DomainException("Bairro é obrigatório para um endereço válido.");
        }
        if (cidade == null || cidade.isBlank()) {
            throw new DomainException("Cidade é obrigatória para um endereço válido.");
        }
        if (cep == null || cep.isBlank()) {
            throw new DomainException("CEP é obrigatório para um endereço válido.");
        }
        if (uf == null || uf.isBlank()) {
            throw new DomainException("UF é obrigatória para um endereço válido.");
        }
    }
}
