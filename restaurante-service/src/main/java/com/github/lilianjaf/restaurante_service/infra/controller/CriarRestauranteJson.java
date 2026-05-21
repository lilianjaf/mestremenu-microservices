package com.github.lilianjaf.restaurante_service.infra.controller;

public record CriarRestauranteJson(
        String nome,
        EnderecoJson endereco,
        String tipoCozinha,
        String horarioFuncionamento
) {
}
