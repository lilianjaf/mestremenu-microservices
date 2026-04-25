package com.github.lilianjaf.restaurante_service.core.usecase;

import com.github.lilianjaf.mestremenuclean.cardapio.core.domain.Cardapio;

import java.util.List;
import java.util.UUID;

public interface BuscarCardapioPorRestauranteUseCase {
    List<Cardapio> executar(UUID idRestaurante);
}
