package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.CardapioSemItensException;

public class CardapioDeveTerPeloMenosUmItemRule implements ValidadorCardapioRule<CardapioRuleContext> {
    @Override
    public void validar(CardapioRuleContext context) {
        if (context.alterouItens() && !context.hasPeloMenosUmItem()) {
            throw new CardapioSemItensException("O cardápio precisa ter no mínimo 1 item.");
        }
    }
}
