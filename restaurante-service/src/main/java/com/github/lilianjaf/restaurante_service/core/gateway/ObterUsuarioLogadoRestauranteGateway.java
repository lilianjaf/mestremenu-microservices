package com.github.lilianjaf.restaurante_service.core.gateway;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Usuario;

import java.util.Optional;

public interface ObterUsuarioLogadoRestauranteGateway {
    Optional<Usuario> obterUsuarioLogado();
}
