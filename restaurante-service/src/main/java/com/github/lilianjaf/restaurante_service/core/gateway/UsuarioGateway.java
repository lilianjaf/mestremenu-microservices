package com.github.lilianjaf.restaurante_service.core.gateway;

import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {
    Optional<Usuario> buscarPorId(UUID id);
    Optional<Usuario> buscarPorUsuario(String username);
}
