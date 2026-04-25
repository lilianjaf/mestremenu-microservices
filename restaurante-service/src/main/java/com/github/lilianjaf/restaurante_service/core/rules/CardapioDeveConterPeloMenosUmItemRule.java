package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.exception.CardapioException;

public class CardapioDeveConterPeloMenosUmItemRule {

    public static void validar(CriacaoCardapioContext context) {
        if (context.dados().itens() == null || context.dados().itens().isEmpty()) {
            throw new CardapioException("O cardápio deve conter no mínimo 1 item.");
        }
    }
}
