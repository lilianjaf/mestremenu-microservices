package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import java.util.UUID;

public interface CriarUsuarioUsecase {
    UUID criar(
            String nome, String email, String login, String senhaPura,
            String nomeTipoDesejado, TipoNativo tipoNativoDesejado,
            String logradouro, String numero, String complemento, String bairro, String cidade, String cep, String uf);
}
