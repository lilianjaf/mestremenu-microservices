package com.github.lilianjaf.usuario_service.core.exception;

public class UsuarioNaoAutenticadoException extends DomainException {
    public UsuarioNaoAutenticadoException(String mensagem) {
        super(mensagem);
    }
}
