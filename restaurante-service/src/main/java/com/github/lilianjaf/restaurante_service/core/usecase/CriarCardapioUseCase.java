package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Cardapio;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoCardapio;

public interface CriarCardapioUseCase {
    Cardapio executar(DadosCriacaoCardapio dados);
}
