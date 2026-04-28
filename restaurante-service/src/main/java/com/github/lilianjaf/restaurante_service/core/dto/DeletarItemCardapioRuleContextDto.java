package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.TipoNativo;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

public record DeletarItemCardapioRuleContextDto(
    Usuario usuarioLogado,
    Restaurante restaurante,
    ItemCardapio item,
    boolean isItemDoProprioRestaurante
) {
    public boolean isUsuarioTipoDono() {
        return usuarioLogado != null && usuarioLogado.getTipoNativo() == TipoNativo.DONO;
    }

    public boolean isItemDoProprioRestaurante() {
        return isItemDoProprioRestaurante;
    }

    public boolean isUsuarioDonoDoRestaurante() {
        return usuarioLogado != null && restaurante != null && usuarioLogado.getId().equals(restaurante.getIdDono());
    }
}
