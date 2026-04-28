package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.restaurante_service.core.domain.Endereco;
import java.util.UUID;

public record DadosCriacaoRestaurante(
        String nome,
        Endereco endereco,
        String tipoCozinha,
        String horarioFuncionamento,
        UUID idDono
) {
}
