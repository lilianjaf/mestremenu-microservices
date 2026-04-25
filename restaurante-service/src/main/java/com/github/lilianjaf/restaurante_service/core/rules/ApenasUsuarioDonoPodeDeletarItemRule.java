package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DeletarItemCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.AcessoNegadoDelecaoItemException;

public class ApenasUsuarioDonoPodeDeletarItemRule implements ValidadorPermissaoCardapioRule<DeletarItemCardapioRuleContextDto> {
    @Override
    public void validar(DeletarItemCardapioRuleContextDto context) {
        if (!context.isUsuarioTipoDono()) {
            throw new AcessoNegadoDelecaoItemException("Acesso negado: apenas usuários com perfil DONO podem deletar itens de cardápio.");
        }

        if (!context.isUsuarioDonoDoRestaurante()) {
            throw new AcessoNegadoDelecaoItemException("Acesso negado: apenas o usuário dono do restaurante pode deletar itens de cardápio.");
        }
    }
}
