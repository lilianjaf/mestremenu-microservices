package com.github.lilianjaf.usuario_service.core.usecase;

import java.util.UUID;

public interface AtualizarUsuarioUsecase {
    void atualizarComEndereco(
            UUID id, String novoNome, String novoEmail,
            String logradouro, String numero, String complemento, String bairro, String cidade, String cep, String uf);

    void atualizarSemEndereco(UUID id, String novoNome, String novoEmail);
}
