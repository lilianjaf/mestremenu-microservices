package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.TipoNativo;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

import com.github.lilianjaf.restaurante_service.core.rules.PermissionRuleContext;

public record DeletarCardapioRuleContextDto(
    Usuario usuarioLogado,
    Restaurante restaurante,
    Cardapio cardapio,
    boolean isCardapioDoProprioRestaurante
) implements PermissionRuleContext {
    public boolean isUsuarioTipoDono() {
        return usuarioLogado != null && usuarioLogado.getTipoNativo() == TipoNativo.DONO;
    }

    public boolean isUsuarioDonoDoRestaurante() {
        return usuarioLogado != null && restaurante != null && usuarioLogado.getId().equals(restaurante.getIdDono());
    }

    public boolean isCardapioDoProprioRestaurante() {
        return isCardapioDoProprioRestaurante;
    }
}
