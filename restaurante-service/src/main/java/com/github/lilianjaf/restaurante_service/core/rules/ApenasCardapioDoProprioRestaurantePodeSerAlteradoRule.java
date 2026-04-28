package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.AlteracaoCardapioNaoAutorizadaException;

public class ApenasCardapioDoProprioRestaurantePodeSerAlteradoRule implements ValidadorCardapioRule<AlterarCardapioRuleContextDto> {
    @Override
    public void validar(AlterarCardapioRuleContextDto context) {
        if (!context.isCardapioDoProprioRestaurante()) {
            throw new AlteracaoCardapioNaoAutorizadaException("O cardápio não pertence ao seu restaurante.");
        }
    }
}
