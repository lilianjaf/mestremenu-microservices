package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.CriarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CriacaoCardapioNaoAutorizadaException;

public class ApenasDonoPodeCriarCardapioParaProprioRestauranteRule implements ValidadorPermissaoCardapioRule<CriarCardapioRuleContextDto> {
    @Override
    public void validar(CriarCardapioRuleContextDto context) {
        if (!context.isUsuarioDonoDoRestaurante()) {
            throw new CriacaoCardapioNaoAutorizadaException("Apenas o dono do restaurante pode criar cardápios.");
        }
    }
}
