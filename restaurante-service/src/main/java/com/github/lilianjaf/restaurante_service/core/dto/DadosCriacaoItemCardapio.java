package com.github.lilianjaf.restaurante_service.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record DadosCriacaoItemCardapio(
        String nome,
        String descricao,
        BigDecimal preco,
        boolean disponibilidadeRestaurante,
        String caminhoFoto,
        UUID idCardapio
) {
}
