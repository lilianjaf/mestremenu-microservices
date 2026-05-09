package com.github.lilianjaf.pedido_service.core.gateway;

import com.github.lilianjaf.pedido_service.core.domain.Usuario;

import java.util.Optional;

public interface ObterUsuarioLogadoGateway {
    Optional<Usuario> obterUsuarioLogado();
}
