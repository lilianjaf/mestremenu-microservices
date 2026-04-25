package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.usuario.core.domain.UsuarioBase;
import com.github.lilianjaf.mestremenuclean.usuario.core.dto.UsuarioOutput;

import java.util.UUID;

public interface BuscarUsuarioUsecase {
    UsuarioOutput buscarPorId(UUID id);
    UsuarioBase buscarEntidade(UUID id);
    UsuarioOutput buscarPorUsername(String username);
}
