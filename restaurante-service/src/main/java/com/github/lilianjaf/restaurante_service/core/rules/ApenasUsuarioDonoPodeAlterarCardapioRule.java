package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.AlterarCardapioRuleContextDto;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.AcessoNegadoAlteracaoCardapioException;

public class ApenasUsuarioDonoPodeAlterarCardapioRule implements ValidadorPermissaoCardapioRule<AlterarCardapioRuleContextDto> {
    @Override
    public void validar(AlterarCardapioRuleContextDto context) {
        if (!context.isUsuarioTipoDono()) {
            throw new AcessoNegadoAlteracaoCardapioException("Apenas usuários com perfil DONO podem alterar cardápios.");
        }
    }
}
