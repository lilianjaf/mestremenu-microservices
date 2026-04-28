package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.AlteracaoItemNaoAutorizadaException;

public class ApenasDonoDoRestaurantePodeAlterarItemRule implements ValidadorPermissaoItemCardapioRule<ItemCardapioRuleContext> {
    @Override
    public void validar(ItemCardapioRuleContext context) {
        if (!context.isUsuarioDonoDoRestauranteDoItem()) {
            throw new AlteracaoItemNaoAutorizadaException("Apenas o dono do restaurante pode alterar itens do cardápio.");
        }
    }
}
