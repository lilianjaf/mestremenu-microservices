package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.NomeCardapioObrigatorioException;

public class CardapioDeveTerNomeRule implements ValidadorCardapioRule<CardapioRuleContext> {
    @Override
    public void validar(CardapioRuleContext context) {
        if (!context.hasNome()) {
            throw new NomeCardapioObrigatorioException("O cardápio precisa ter um nome preenchido.");
        }
    }
}
