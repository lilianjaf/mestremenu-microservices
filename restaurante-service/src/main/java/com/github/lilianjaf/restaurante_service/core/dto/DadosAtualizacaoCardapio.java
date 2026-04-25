package com.github.lilianjaf.restaurante_service.core.dto;

import java.util.List;
import java.util.UUID;

public record DadosAtualizacaoCardapio(
        UUID idCardapio,
        String nome,
        List<DadosCriacaoItemCardapio> itens
) {
}
