package com.github.lilianjaf.usuario_service.infra.controller;

import java.util.UUID;

public record UsuarioResponseJson(
        UUID id,
        String nome,
        String email,
        String login,
        String tipoPerfil,
        String tipoNativo,
        Boolean ativo
) {
}