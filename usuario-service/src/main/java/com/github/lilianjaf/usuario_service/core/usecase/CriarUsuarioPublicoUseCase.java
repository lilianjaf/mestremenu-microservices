package com.github.lilianjaf.usuario_service.core.usecase;

import java.util.UUID;

public interface CriarUsuarioPublicoUseCase {
    UUID criar(
            String nome, String email, String login, String senhaPura,
            String logradouro, String numero, String complemento, String bairro, String cidade, String cep, String uf);
}
