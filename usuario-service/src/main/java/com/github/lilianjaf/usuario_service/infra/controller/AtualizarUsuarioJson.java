package com.github.lilianjaf.usuario_service.infra.controller;

public record AtualizarUsuarioJson(
        String nome,
        String email,
        EnderecoJson endereco
) {
}