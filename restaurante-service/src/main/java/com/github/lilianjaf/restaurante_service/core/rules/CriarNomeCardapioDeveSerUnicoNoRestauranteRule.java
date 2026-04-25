package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.CriarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.NomeCardapioJaEmUsoException;

public class CriarNomeCardapioDeveSerUnicoNoRestauranteRule implements ValidadorCardapioRule<CriarCardapioRuleContextDto> {
    
    @Override
    public void validar(CriarCardapioRuleContextDto context) {
        if (!context.isNomeUnico()) {
            throw new NomeCardapioJaEmUsoException("Não podem existir dois cardápios com o mesmo nome dentro do mesmo restaurante.");
        }
    }
}
