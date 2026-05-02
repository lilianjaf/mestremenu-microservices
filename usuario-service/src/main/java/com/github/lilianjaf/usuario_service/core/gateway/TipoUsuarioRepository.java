package com.github.lilianjaf.usuario_service.core.gateway;

import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;

import java.util.Optional;
import java.util.UUID;

public interface TipoUsuarioRepository {
    Optional<TipoUsuario> findByNome(String nome);
    TipoUsuario salvar(TipoUsuario tipoUsuario);
    Optional<TipoUsuario> findById(UUID id);
    void deletar(TipoUsuario tipo);
}