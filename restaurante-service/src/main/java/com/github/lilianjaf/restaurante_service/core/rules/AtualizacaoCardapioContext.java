package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Cardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosAtualizacaoCardapio;

public record AtualizacaoCardapioContext(
        Usuario usuarioLogado,
        Restaurante restaurante,
        Cardapio cardapioExistente,
        DadosAtualizacaoCardapio dados
) {
}
