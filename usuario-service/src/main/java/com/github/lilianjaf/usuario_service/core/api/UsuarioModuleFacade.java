package com.github.lilianjaf.usuario_service.core.api;

import java.util.UUID;

public interface UsuarioModuleFacade {
    UsuarioIntegrationDto buscarUsuarioParaIntegracao(UUID id);
    UsuarioIntegrationDto buscarPorUsuario(String username);
}