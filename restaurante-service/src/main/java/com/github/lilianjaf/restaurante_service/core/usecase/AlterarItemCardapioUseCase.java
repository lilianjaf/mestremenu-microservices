package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.ItemCardapio;
import com.github.lilianjaf.mestremenuclean.cardapio.core.dto.DadosAtualizacaoItemCardapio;

public interface AlterarItemCardapioUseCase {
    ItemCardapio executar(DadosAtualizacaoItemCardapio dados);
}
