package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.restaurante_service.core.domain.Restaurante;

import java.util.List;

public interface ListarRestaurantesUseCase {
    List<Restaurante> executar();
}
