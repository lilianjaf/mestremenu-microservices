package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.NomeCardapioJaEmUsoException;

public class NomeCardapioDeveSerUnicoNoRestauranteRule implements ValidadorCardapioRule<CardapioRuleContext> {
    @Override
    public void validar(CardapioRuleContext context) {
        if (!context.isNomeUnico()) {
            throw new NomeCardapioJaEmUsoException("Já existe um cardápio com este nome para este restaurante.");
        }
    }
}
