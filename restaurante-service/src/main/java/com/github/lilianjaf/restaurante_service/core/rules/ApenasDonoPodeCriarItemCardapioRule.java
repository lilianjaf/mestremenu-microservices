package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.TipoNativo;
import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CardapioException;

public class ApenasDonoPodeCriarItemCardapioRule implements ValidadorPermissaoItemCardapioRule<CriacaoItemCardapioContext> {

    @Override
    public void validar(CriacaoItemCardapioContext context) {
        if (!context.usuarioLogado().getTipoNativo().equals(TipoNativo.DONO)) {
            throw new CardapioException("Apenas usuários com perfil de DONO podem criar itens no cardápio.");
        }
    }
}
