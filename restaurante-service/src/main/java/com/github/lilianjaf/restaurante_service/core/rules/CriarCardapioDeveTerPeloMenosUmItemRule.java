package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.CriarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CardapioSemItensException;

public class CriarCardapioDeveTerPeloMenosUmItemRule implements ValidadorCardapioRule<CriarCardapioRuleContextDto> {
    
    @Override
    public void validar(CriarCardapioRuleContextDto context) {
        if (!context.hasPeloMenosUmItem()) {
            throw new CardapioSemItensException("O cardápio precisa ter no mínimo 1 item.");
        }
    }
}
