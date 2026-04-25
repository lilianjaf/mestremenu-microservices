package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Cardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.TipoNativo;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;

public record DeletarCardapioRuleContextDto(
    Usuario usuarioLogado,
    Restaurante restaurante,
    Cardapio cardapio,
    boolean isCardapioDoProprioRestaurante
) {
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
