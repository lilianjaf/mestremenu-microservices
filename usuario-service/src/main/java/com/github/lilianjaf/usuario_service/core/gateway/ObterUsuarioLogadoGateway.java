package com.github.lilianjaf.usuario_service.core.gateway;

import com.github.lilianjaf.mestremenuclean.usuario.core.domain.UsuarioBase;

import java.util.Optional;

public interface ObterUsuarioLogadoGateway {
    Optional<UsuarioBase> obterUsuarioLogado();
}