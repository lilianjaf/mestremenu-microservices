package com.github.lilianjaf.restaurante_service.infra.controller;

public record EnderecoJson(
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String cep,
        String uf
) {
}
