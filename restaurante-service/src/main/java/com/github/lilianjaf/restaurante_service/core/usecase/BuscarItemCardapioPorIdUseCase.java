package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.ItemCardapio;

import java.util.UUID;

public interface BuscarItemCardapioPorIdUseCase {
    ItemCardapio executar(UUID id);
}
