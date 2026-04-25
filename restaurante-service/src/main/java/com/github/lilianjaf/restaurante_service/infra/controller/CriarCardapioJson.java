package com.github.lilianjaf.restaurante_service.infra.controller;

import java.util.List;
import java.util.UUID;

public record CriarCardapioJson(
        String nome,
        UUID idRestaurante,
        List<CriarItemCardapioJson> itens,
        UUID idUsuarioLogado
) {
}
