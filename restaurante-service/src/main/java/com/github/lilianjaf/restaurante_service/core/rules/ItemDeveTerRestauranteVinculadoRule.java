package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.RestauranteNaoVinculadoAoItemException;

public class ItemDeveTerRestauranteVinculadoRule implements ValidadorPermissaoItemCardapioRule<ItemCardapioRuleContext> {
    @Override
    public void validar(ItemCardapioRuleContext context) {
        if (!context.hasRestauranteVinculado()) {
            throw new RestauranteNaoVinculadoAoItemException("O item deve estar vinculado a um restaurante válido.");
        }
    }
}
