package com.github.lilianjaf.restaurante_service.core.api;

import java.util.UUID;

public interface RestauranteModuleFacade {
    RestauranteIntegrationDto buscarPorId(UUID id);
}
