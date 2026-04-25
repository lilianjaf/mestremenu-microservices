package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.restaurante.core.exception.EdicaoRestauranteNaoAutorizadaException;

public class ApenasDonoDoRestaurantePodeAtualizarRule implements AtualizarRestauranteRule {
    @Override
    public void validar(AtualizarRestauranteRuleContextDto context) {
        if (!context.isUsuarioLogadoProprietarioDoRestaurante()) {
            throw new EdicaoRestauranteNaoAutorizadaException("Apenas o proprietário pode alterar os dados do restaurante.");
        }
    }
}
