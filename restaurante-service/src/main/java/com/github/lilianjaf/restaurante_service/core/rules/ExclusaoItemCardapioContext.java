package com.github.lilianjaf.restaurante_service.core.rules;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.domain.Usuario;

public record ExclusaoItemCardapioContext(
        Usuario usuarioLogado,
        Restaurante restaurante,
        ItemCardapio itemExistente
) {
}
