package com.github.lilianjaf.usuario_service.core.exception;

public class EdicaoUsuarioNaoAutorizadaException extends AcessoNegadoException {
    public EdicaoUsuarioNaoAutorizadaException(String mensagem) {
        super(mensagem);
    }
}
