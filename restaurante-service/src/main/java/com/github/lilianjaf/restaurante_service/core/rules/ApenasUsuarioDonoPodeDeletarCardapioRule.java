package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DeletarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.AcessoNegadoDelecaoCardapioException;

public class ApenasUsuarioDonoPodeDeletarCardapioRule implements ValidadorPermissaoCardapioRule<DeletarCardapioRuleContextDto> {
    @Override
    public void validar(DeletarCardapioRuleContextDto context) {
        if (!context.isUsuarioTipoDono()) {
            throw new AcessoNegadoDelecaoCardapioException("Acesso negado: apenas usuários com perfil DONO podem deletar cardápios.");
        }
        if (!context.isUsuarioDonoDoRestaurante()) {
            throw new AcessoNegadoDelecaoCardapioException("Acesso negado: apenas o usuário dono do restaurante pode deletar cardápios.");
        }
    }
}
