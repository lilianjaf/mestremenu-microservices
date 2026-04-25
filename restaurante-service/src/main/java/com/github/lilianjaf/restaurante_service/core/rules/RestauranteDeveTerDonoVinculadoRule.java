package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.RestauranteSemDonoException;

public class RestauranteDeveTerDonoVinculadoRule implements AtualizarRestauranteRule, ValidadorCriacaoRestauranteRule {
    @Override
    public void validar(AtualizarRestauranteRuleContextDto context) {
        if (!context.restaurantePossuiDono()) {
            throw new RestauranteSemDonoException("O restaurante deve possuir um dono vinculado.");
        }
    }

    @Override
    public void validar(CriacaoRestauranteContext context) {
        if (!context.hasDonoVinculado()) {
            throw new RestauranteSemDonoException("O restaurante deve possuir um dono vinculado para ser criado.");
        }
    }
}
