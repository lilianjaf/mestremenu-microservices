package com.github.lilianjaf.restaurante_service.infra.controller;

import java.util.UUID;

public record RestauranteResponseJson(
        UUID id,
        String nome,
        String tipoCozinha,
        String horarioFuncionamento,
        UUID idDono
) {
}
