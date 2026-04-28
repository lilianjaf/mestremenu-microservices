package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoCardapio;

public record AtualizacaoCardapioContext(
        Usuario usuarioLogado,
        Restaurante restaurante,
        Cardapio cardapioExistente,
        DadosAtualizacaoCardapio dados
) {
}
