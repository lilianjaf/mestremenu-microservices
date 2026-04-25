package com.github.lilianjaf.restaurante_service.infra.gateway;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.cardapio.core.gateway.RestauranteGateway;
import com.github.lilianjaf.mestremenuclean.restaurante.core.api.RestauranteIntegrationDto;
import com.github.lilianjaf.mestremenuclean.restaurante.core.api.RestauranteModuleFacade;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class RestauranteGatewayImpl implements RestauranteGateway {

    private final RestauranteModuleFacade restauranteFacade;

    public RestauranteGatewayImpl(RestauranteModuleFacade restauranteFacade) {
        this.restauranteFacade = restauranteFacade;
    }

    @Override
    public Optional<Restaurante> buscarPorId(UUID id) {
        RestauranteIntegrationDto dto = restauranteFacade.buscarPorId(id);

        if (dto == null) {
            return Optional.empty();
        }

        return Optional.of(new Restaurante(dto.id(), dto.idDono()));
    }
}
