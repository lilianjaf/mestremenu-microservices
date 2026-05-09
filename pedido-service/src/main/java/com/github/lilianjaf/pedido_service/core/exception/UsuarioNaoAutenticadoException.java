package com.github.lilianjaf.pedido_service.core.exception;

public class UsuarioNaoAutenticadoException extends DomainException {
    public UsuarioNaoAutenticadoException(String mensagem) {
        super(mensagem);
    }
}
