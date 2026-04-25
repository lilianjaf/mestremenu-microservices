package com.github.lilianjaf.usuario_service.core.dto;

import java.util.UUID;

public record UsuarioOutput(
        UUID id,
        String nome,
        String email,
        String login,
        String tipoPerfil,
        String tipoNativo,
        Boolean ativo
) {
}