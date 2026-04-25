package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DeletarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.DelecaoCardapioNaoAutorizadaException;

public class ApenasCardapioDoProprioRestaurantePodeSerDeletadoRule implements ValidadorCardapioRule<DeletarCardapioRuleContextDto> {
    @Override
    public void validar(DeletarCardapioRuleContextDto context) {
        if (!context.isCardapioDoProprioRestaurante()) {
            throw new DelecaoCardapioNaoAutorizadaException("Deleção não autorizada: o cardápio não pertence a um restaurante de sua propriedade.");
        }
    }
}
