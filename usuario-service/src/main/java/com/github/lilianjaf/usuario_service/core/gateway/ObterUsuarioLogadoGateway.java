package com.github.lilianjaf.usuario_service.core.gateway;

import com.github.lilianjaf.usuario_service.core.domain.UsuarioBase;

import java.util.Optional;

public interface ObterUsuarioLogadoGateway {
    Optional<UsuarioBase> obterUsuarioLogado();
}