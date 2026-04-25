package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.restaurante.core.api.RestauranteIntegrationDto;
import com.github.lilianjaf.mestremenuclean.restaurante.core.api.RestauranteModuleFacade;
import com.github.lilianjaf.mestremenuclean.restaurante.core.gateway.RestauranteRepository;

import java.util.UUID;

public class RestauranteModuleFacadeImpl implements RestauranteModuleFacade {

    private final RestauranteRepository restauranteRepository;

    public RestauranteModuleFacadeImpl(RestauranteRepository restauranteRepository) {
        this.restauranteRepository = restauranteRepository;
    }

    @Override
    public RestauranteIntegrationDto buscarPorId(UUID id) {
        return restauranteRepository.findById(id)
                .map(restaurante -> new RestauranteIntegrationDto(
                        restaurante.getId(),
                        restaurante.getIdDono(),
                        restaurante.isAtivo()
                ))
                .orElse(null);
    }
}
