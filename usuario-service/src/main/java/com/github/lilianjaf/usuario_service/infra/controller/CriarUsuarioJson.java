package com.github.lilianjaf.usuario_service.infra.controller;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;

public record CriarUsuarioJson(
        String nome,
        String email,
        String login,
        String senha,
        String nomeTipoDesejado,
        TipoNativo tipoNativo,
        EnderecoJson endereco
) {
}