package com.github.lilianjaf.restaurante_service.core.dto;

import java.util.List;
import java.util.UUID;

public record DadosCriacaoCardapio(
        String nome,
        UUID idRestaurante,
        List<DadosCriacaoItemCardapio> itens
) {
}
