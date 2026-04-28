package com.github.lilianjaf.restaurante_service.core.dto;

import com.github.lilianjaf.restaurante_service.core.domain.Endereco;

public record DadosAtualizacaoRestaurante(
        String nome,
        Endereco endereco,
        String tipoCozinha,
        String horarioFuncionamento
) {
}
