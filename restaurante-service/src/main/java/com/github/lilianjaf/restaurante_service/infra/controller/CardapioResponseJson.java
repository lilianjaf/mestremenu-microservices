package com.github.lilianjaf.restaurante_service.infra.controller;

import java.util.UUID;

public record CardapioResponseJson(
        UUID id,
        String nome,
        UUID idRestaurante
) {
}
