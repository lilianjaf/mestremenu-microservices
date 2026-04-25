package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DeletarItemCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.DelecaoItemNaoAutorizadaException;

public class ApenasDonoDoRestaurantePodeDeletarItemRule implements ValidadorPermissaoCardapioRule<DeletarItemCardapioRuleContextDto> {
    @Override
    public void validar(DeletarItemCardapioRuleContextDto context) {
        if (!context.isItemDoProprioRestaurante()) {
            throw new DelecaoItemNaoAutorizadaException("Deleção não autorizada: o usuário não é o dono do restaurante deste item.");
        }
    }
}
