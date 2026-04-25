package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CardapioSemItensException;

public class CardapioDeveTerPeloMenosUmItemRule implements ValidadorCardapioRule<AlterarCardapioRuleContextDto> {
    @Override
    public void validar(AlterarCardapioRuleContextDto context) {
        if (context.alterouItens() && !context.hasPeloMenosUmItem()) {
            throw new CardapioSemItensException("O cardápio precisa ter no mínimo 1 item.");
        }
    }
}
