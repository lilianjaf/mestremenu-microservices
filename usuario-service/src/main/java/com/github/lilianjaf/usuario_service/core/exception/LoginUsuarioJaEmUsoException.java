package com.github.lilianjaf.usuario_service.core.exception;

public class LoginUsuarioJaEmUsoException extends RegraDeNegocioException {
    public LoginUsuarioJaEmUsoException(String mensagem) {
        super(mensagem);
    }
}
