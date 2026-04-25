package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;
import java.util.UUID;

public interface BuscarRestaurantePorIdUseCase {
    Restaurante executar(UUID id);
}
