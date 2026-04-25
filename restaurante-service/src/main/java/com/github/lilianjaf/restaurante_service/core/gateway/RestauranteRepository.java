package com.github.lilianjaf.restaurante_service.core.gateway;

import com.github.lilianjaf.mestremenuclean.restaurante.core.domain.Restaurante;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestauranteRepository {
    Restaurante salvar(Restaurante restaurante);
    Optional<Restaurante> findById(UUID id);
    List<Restaurante> buscarTodos();
}
