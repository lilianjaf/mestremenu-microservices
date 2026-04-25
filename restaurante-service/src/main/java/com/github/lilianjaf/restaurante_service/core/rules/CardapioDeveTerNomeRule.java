package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.NomeCardapioObrigatorioException;

public class CardapioDeveTerNomeRule implements ValidadorCardapioRule<AlterarCardapioRuleContextDto> {
    @Override
    public void validar(AlterarCardapioRuleContextDto context) {
        if (!context.hasNome()) {
            throw new NomeCardapioObrigatorioException("O cardápio precisa ter um nome preenchido.");
        }
    }
}
