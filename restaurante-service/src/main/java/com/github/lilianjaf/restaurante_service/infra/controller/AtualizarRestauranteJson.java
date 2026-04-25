package com.github.lilianjaf.restaurante_service.infra.controller;

public record AtualizarRestauranteJson(
        String nome,
        EnderecoJson endereco,
        String tipoCozinha,
        String horarioFuncionamento
) {
    public record EnderecoJson(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String cep,
            String uf
    ) {}
}
