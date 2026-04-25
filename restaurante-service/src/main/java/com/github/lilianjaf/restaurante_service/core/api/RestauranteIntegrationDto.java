package com.github.lilianjaf.restaurante_service.core.api;

import java.util.UUID;

public record RestauranteIntegrationDto(UUID id, UUID idDono, boolean ativo) {}
