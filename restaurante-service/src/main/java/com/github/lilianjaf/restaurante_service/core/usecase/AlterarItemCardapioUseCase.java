package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.ItemCardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoItemCardapio;

public interface AlterarItemCardapioUseCase {
    ItemCardapio executar(DadosAtualizacaoItemCardapio dados);
}
