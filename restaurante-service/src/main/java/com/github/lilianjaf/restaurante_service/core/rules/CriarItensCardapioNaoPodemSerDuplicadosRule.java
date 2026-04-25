package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.CriarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.ItensCardapioDuplicadosException;

public class CriarItensCardapioNaoPodemSerDuplicadosRule implements ValidadorCardapioRule<CriarCardapioRuleContextDto> {
    
    @Override
    public void validar(CriarCardapioRuleContextDto context) {
        if (context.hasItensDuplicados()) {
            throw new ItensCardapioDuplicadosException("A lista de itens contém itens duplicados.");
        }
    }
}
