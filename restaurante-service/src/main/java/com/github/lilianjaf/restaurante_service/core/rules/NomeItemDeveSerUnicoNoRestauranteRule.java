package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.NomeItemJaEmUsoException;

public class NomeItemDeveSerUnicoNoRestauranteRule implements ValidadorItemCardapioRule<ItemCardapioRuleContext> {
    @Override
    public void validar(ItemCardapioRuleContext context) {
        if (!context.isNomeUnico()) {
            throw new NomeItemJaEmUsoException("Já existe um item com este nome neste restaurante.");
        }
    }
}
