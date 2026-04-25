package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Restaurante;
import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Usuario;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosCriacaoCardapio;

public record CriacaoCardapioContext(
        Usuario usuarioLogado,
        Restaurante restaurante,
        DadosCriacaoCardapio dados
) {
}
