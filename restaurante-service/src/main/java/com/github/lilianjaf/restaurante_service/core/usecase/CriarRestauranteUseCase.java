package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.dto.DadosCriacaoRestaurante;

public interface CriarRestauranteUseCase {
    Restaurante executar(DadosCriacaoRestaurante dados);
}
