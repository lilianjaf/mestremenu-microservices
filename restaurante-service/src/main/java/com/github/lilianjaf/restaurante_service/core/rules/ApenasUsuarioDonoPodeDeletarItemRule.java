package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.dto.DeletarItemCardapioRuleContextDto;
import com.github.lilianjaf.restaurante_service.core.exception.DelecaoItemNaoAutorizadaException;

public class ApenasUsuarioDonoPodeDeletarItemRule implements ValidadorPermissaoCardapioRule<DeletarItemCardapioRuleContextDto> {
    @Override
    public void validar(DeletarItemCardapioRuleContextDto context) {
        if (!context.isUsuarioTipoDono()) {
            throw new DelecaoItemNaoAutorizadaException("Acesso negado: apenas usuários com perfil DONO podem deletar itens de cardápio.");
        }

        if (!context.isUsuarioDonoDoRestaurante()) {
            throw new DelecaoItemNaoAutorizadaException("Acesso negado: apenas o usuário dono do restaurante pode deletar itens de cardápio.");
        }
    }
}
