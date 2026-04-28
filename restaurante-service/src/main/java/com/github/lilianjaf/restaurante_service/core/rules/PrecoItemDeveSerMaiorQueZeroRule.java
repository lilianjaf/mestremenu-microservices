package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.PrecoItemInvalidoException;

public class PrecoItemDeveSerMaiorQueZeroRule implements ValidadorItemCardapioRule<ItemCardapioRuleContext> {
    @Override
    public void validar(ItemCardapioRuleContext context) {
        if (!context.isPrecoValido()) {
            throw new PrecoItemInvalidoException("O preço do item deve ser maior que zero.");
        }
    }
}
