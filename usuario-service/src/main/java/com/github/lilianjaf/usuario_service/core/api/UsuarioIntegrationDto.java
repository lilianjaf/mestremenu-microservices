package com.github.lilianjaf.usuario_service.core.api;

import java.util.UUID;

public record UsuarioIntegrationDto(UUID id, String nomeDoTipo, String tipoNativo, boolean ativo) {}