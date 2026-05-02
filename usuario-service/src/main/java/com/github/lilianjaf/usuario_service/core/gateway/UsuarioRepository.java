package com.github.lilianjaf.usuario_service.core.gateway;

import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;
import com.github.lilianjaf.usuario_service.core.dto.UsuarioOutput;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {
    UsuarioBase salvar(UsuarioBase usuario);
    Optional<UsuarioBase> findByLogin(String login);
    Optional<UsuarioOutput> findUserByLogin(String login);
    Optional<UsuarioBase> findById(UUID id);
    Optional<UsuarioBase> findByEmail(String email);
    boolean existeUsuarioComTipo(UUID idTipoUsuario);
}