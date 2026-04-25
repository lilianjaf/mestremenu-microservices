package com.github.lilianjaf.restaurante_service.infra.controller;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemCardapioResponseJson(
        UUID id,
        String nome,
        String descricao,
        BigDecimal preco,
        boolean disponibilidadeRestaurante,
        String caminhoFoto,
        UUID idCardapio
) {
}
