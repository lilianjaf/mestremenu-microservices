package com.github.lilianjaf.usuario_service.infra.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarUsuarioPublicoJson(
        @NotBlank String nome,
        @NotBlank String email,
        @NotBlank String login,
        @NotBlank String senha,
        @NotNull @Valid EnderecoJson endereco
) {
}
