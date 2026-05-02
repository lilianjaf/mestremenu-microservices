package com.github.lilianjaf.usuario_service.core.usecase;

import com.github.lilianjaf.usuario_service.core.domain.TipoNativo;
import com.github.lilianjaf.usuario_service.core.domain.TipoUsuario;
public interface CriarTipoUsuarioUsecase {
    TipoUsuario criar(String loginUsuarioLogado, String nome, TipoNativo tipoNativo);
}
