package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.exception.CardapioException;

public class ApenasDonoPodeGerenciarCardapioRule implements ValidadorPermissaoCardapioRule<PermissionRuleContext> {
    @Override
    public void validar(PermissionRuleContext context) {
        if (!context.isUsuarioDonoDoRestaurante()) {
            throw new CardapioException("Apenas o dono do restaurante pode gerenciar cardápios.");
        }
    }
}
