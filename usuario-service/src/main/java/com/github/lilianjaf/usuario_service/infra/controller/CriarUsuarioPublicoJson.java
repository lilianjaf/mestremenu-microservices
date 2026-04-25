package com.github.lilianjaf.usuario_service.infra.controller;

public record CriarUsuarioPublicoJson(
        String nome,
        String email,
        String login,
        String senha,
        EnderecoJson endereco
) {
}
