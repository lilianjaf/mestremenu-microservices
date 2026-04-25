package com.github.lilianjaf.restaurante_service.infra.controller;

import java.util.UUID;

public record AtualizarCardapioJson(
        String nome,
        UUID idUsuarioLogado
) {
}
