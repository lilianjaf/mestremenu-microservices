package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.ItemCardapioRuleContext;
import com.github.lilianjaf.restaurante_service.core.exception.DadosObrigatoriosItemIncompletosException;

public class CamposObrigatoriosItemRule implements ValidadorItemCardapioRule<ItemCardapioRuleContext> {
    @Override
    public void validar(ItemCardapioRuleContext context) {
        if (!context.hasTodosCamposPreenchidos()) {
            throw new DadosObrigatoriosItemIncompletosException("Todos os dados essenciais do item devem estar preenchidos.");
        }
    }
}
