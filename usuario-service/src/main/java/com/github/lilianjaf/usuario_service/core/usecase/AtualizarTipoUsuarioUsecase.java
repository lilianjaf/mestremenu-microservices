package com.github.lilianjaf.usuario_service.core.usecase;

import java.util.UUID;

public interface AtualizarTipoUsuarioUsecase {
    void atualizar(UUID id, String novoNome);
}
