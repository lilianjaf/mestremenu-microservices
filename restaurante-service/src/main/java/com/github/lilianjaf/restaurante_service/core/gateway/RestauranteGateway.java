package com.github.lilianjaf.restaurante_service.core.gateway;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Restaurante;

import java.util.Optional;
import java.util.UUID;

public interface RestauranteGateway {
    Optional<Restaurante> buscarPorId(UUID id);
}
