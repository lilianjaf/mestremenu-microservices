package com.github.lilianjaf.usuario_service.infra.controller;

import jakarta.validation.constraints.NotBlank;

public record EnderecoJson(
        @NotBlank String logradouro,
        @NotBlank String numero,
        String complemento,
        @NotBlank String bairro,
        @NotBlank String cidade,
        @NotBlank String cep,
        @NotBlank String uf
) {
}