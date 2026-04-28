package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoCardapio;

public interface AlterarCardapioUseCase {
    Cardapio executar(DadosAtualizacaoCardapio dados);
}
