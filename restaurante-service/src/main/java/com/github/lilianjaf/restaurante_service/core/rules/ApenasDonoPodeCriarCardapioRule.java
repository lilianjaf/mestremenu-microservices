package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.exception.CriacaoCardapioNaoAutorizadaException;

public class ApenasDonoPodeCriarCardapioRule implements ValidadorPermissaoCardapioRule<PermissionRuleContext> {
    @Override
    public void validar(PermissionRuleContext context) {
        if (!context.isUsuarioDonoDoRestaurante()) {
            throw new CriacaoCardapioNaoAutorizadaException("Apenas o dono do restaurante pode criar cardápios.");
        }
    }
}
