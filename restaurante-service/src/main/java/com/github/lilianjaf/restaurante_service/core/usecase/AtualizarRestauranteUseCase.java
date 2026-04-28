package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;
import com.github.lilianjaf.restaurante_service.core.dto.DadosAtualizacaoRestaurante;
import java.util.UUID;

public interface AtualizarRestauranteUseCase {
    Restaurante executar(UUID id, DadosAtualizacaoRestaurante dados);
}
