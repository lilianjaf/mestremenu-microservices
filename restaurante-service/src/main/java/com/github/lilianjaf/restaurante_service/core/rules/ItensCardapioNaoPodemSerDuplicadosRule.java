package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.ItensCardapioDuplicadosException;

public class ItensCardapioNaoPodemSerDuplicadosRule implements ValidadorCardapioRule<AlterarCardapioRuleContextDto> {
    @Override
    public void validar(AlterarCardapioRuleContextDto context) {
        if (context.hasItensDuplicados()) {
            throw new ItensCardapioDuplicadosException("A nova lista de itens enviada na requisição de alteração não pode conter itens duplicados.");
        }
    }
}
