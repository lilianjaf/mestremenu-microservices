package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.ItensCardapioDuplicadosException;

public class ItensCardapioNaoPodemSerDuplicadosRule implements ValidadorCardapioRule<CardapioRuleContext> {
    @Override
    public void validar(CardapioRuleContext context) {
        if (context.hasItensDuplicados()) {
            throw new ItensCardapioDuplicadosException("O cardápio não pode conter itens duplicados.");
        }
    }
}
