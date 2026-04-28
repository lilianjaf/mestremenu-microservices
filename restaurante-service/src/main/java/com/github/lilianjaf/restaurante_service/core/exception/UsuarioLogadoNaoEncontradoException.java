package com.github.lilianjaf.restaurante_service.core.exception;

import com.github.lilianjaf.restaurante_service.core.exception.RegraDeNegocioException;

public class UsuarioLogadoNaoEncontradoException extends RegraDeNegocioException {
    public UsuarioLogadoNaoEncontradoException(String message) {
        super(message);
    }
}
